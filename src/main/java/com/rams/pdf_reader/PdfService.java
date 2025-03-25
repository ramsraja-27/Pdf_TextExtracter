package com.rams.pdf_reader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    public String extractText(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded", ".pdf");
        file.transferTo(tempFile);

        try (PDDocument document = PDDocument.load(tempFile)) {
            return new PDFTextStripper().getText(document);
        }
    }
}
