package com.finsight.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PdfExtractionService {

    public List<PageContent> extractPages(InputStream pdfStream) {
        List<PageContent> pages = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(pdfStream.readAllBytes())) {
            int totalPages = document.getNumberOfPages();
            log.info("Extracting text from PDF with {} pages", totalPages);

            PDFTextStripper stripper = new PDFTextStripper();

            for (int pageNum = 1; pageNum <= totalPages; pageNum++) {
                stripper.setStartPage(pageNum);
                stripper.setEndPage(pageNum);

                String pageText = stripper.getText(document).trim();

                if (!pageText.isEmpty()) {
                    pages.add(new PageContent(pageNum, pageText));
                    log.debug("Extracted {} characters from page {}", pageText.length(), pageNum);
                } else {
                    log.debug("Page {} is empty or non-text, skipping", pageNum);
                }
            }

            log.info("Successfully extracted text from {}/{} pages", pages.size(), totalPages);

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: " + e.getMessage(), e);
        }

        return pages;
    }

    public String extractFullText(InputStream pdfStream) {
        try (PDDocument document = Loader.loadPDF(pdfStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: " + e.getMessage(), e);
        }
    }

    public int countPages(InputStream pdfStream) {
        try (PDDocument document = Loader.loadPDF(pdfStream.readAllBytes())) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF page count: " + e.getMessage(), e);
        }
    }

    public record PageContent(int pageNumber, String text) {}
}