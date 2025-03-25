package com.rams.pdf_reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIService {
    private static final String GOOSE_AI_KEY = "sk-vvc8F9Tj2bvM5ztizmXU1kKLBWkOtmPHgzPU9Nv50qJPhI4y";
    private static final String GOOSE_AI_URL = "https://api.goose.ai/v1/completions";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, String> processTextWithLLM(String text) throws IOException {
        String model = "gpt-j-6b"; // Ensure this model is available in GooseAI

        // Construct the JSON request properly
        String jsonRequest = mapper.writeValueAsString(Map.of(
                "model", model,
                "prompt", "Extract name, email, opening balance, and closing balance from this CASA statement:\n" + text,
                "max_tokens", 200,
                "temperature", 0.5
        ));

        // Create HTTP Request
        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(GOOSE_AI_URL)
                .header("Authorization", "Bearer " + GOOSE_AI_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        // Send Request
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();  // Store the response once

            if (!response.isSuccessful()) {
                throw new IOException("GooseAI API error: " + responseBody);
            }

            return extractDataFromResponse(responseBody);
        }
    }

    // Extract data from GooseAI response safely
    private Map<String, String> extractDataFromResponse(String responseBody) throws IOException {
        JsonNode jsonNode = mapper.readTree(responseBody);
        JsonNode choicesNode = jsonNode.path("choices");

        if (!choicesNode.isArray() || choicesNode.isEmpty()) {
            throw new IOException("Invalid response format from GooseAI.");
        }

        String extractedText = choicesNode.get(0).path("text").asText().trim();
        if (extractedText.isEmpty()) {
            throw new IOException("GooseAI returned an empty response.");
        }

        // Parse extracted text into key-value pairs
        Map<String, String> extractedData = new HashMap<>();
        String[] lines = extractedText.split("\n");

        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                extractedData.put(parts[0].trim(), parts[1].trim());
            }
        }

        return extractedData;
    }

}


