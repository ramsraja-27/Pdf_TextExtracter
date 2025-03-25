package com.rams.pdf_reader;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfReaderController {

    private final PdfService pdfService;
    private final OpenAIService openAIService;

    public PdfReaderController(PdfService pdfService, OpenAIService openAIService) {
        this.pdfService = pdfService;
        this.openAIService = openAIService;
    }

    @PostMapping("/extract")
    public ResponseEntity<?> extractDetails(@RequestParam("file") MultipartFile file) {
        try {
            String text = pdfService.extractText(file);
            Map<String, String> details = openAIService.processTextWithLLM(text);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing PDF: " + e.getMessage());
        }
    }
}
