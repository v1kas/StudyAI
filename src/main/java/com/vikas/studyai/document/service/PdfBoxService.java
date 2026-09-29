package com.vikas.studyai.document.service;

import com.vikas.studyai.common.exception.InvalidDocumentException;
import com.vikas.studyai.document.model.PageContent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfBoxService implements PdfService {
    @Override
    public List<PageContent> extract(Path file) {
        try (PDDocument pdf = PDDocument.load(file.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            List<PageContent> pages = new ArrayList<>();
            for (int pageNumber = 1; pageNumber <= pdf.getNumberOfPages(); pageNumber++) {
                stripper.setStartPage(pageNumber);
                stripper.setEndPage(pageNumber);
                String content = stripper.getText(pdf)
                        .replaceAll("[\\t\\x0B\\f\\r ]+", " ")
                        .replaceAll("\\n{3,}", "\\n\\n")
                        .trim();
                if (!content.isBlank()) {
                    pages.add(new PageContent(pageNumber, content));
                }
            }
            return List.copyOf(pages);
        } catch (IOException exception) {
            throw new InvalidDocumentException("Unable to extract text from the PDF", exception);
        }
    }
}
