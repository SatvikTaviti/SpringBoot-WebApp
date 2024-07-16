package com.example.demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PdfExportService {

    @Autowired
    private UserRepository userRepository;

    public File exportUsersToPdf() throws IOException {
        List<User> users = userRepository.findAll();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("List of Users");
        contentStream.endText();

        float margin = 50;
        float yStart = 650;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;
        float rowHeight = 20f;
        float tableMargin = 10f;

        String[] headers = {"User ID", "Email", "First Name", "Last Name"};

        // Draw table headers
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        for (String header : headers) {
            contentStream.showText(header);
            contentStream.newLineAtOffset(100, 0);
        }
        contentStream.endText();

        // Draw table content
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (User user : users) {
            yPosition -= tableMargin;
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(String.valueOf(user.getId()));
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(user.getEmail());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(user.getFirstname());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(user.getLastname());
            contentStream.endText();
            yPosition -= rowHeight;
        }

        contentStream.close();
        File tempFile = File.createTempFile("users", ".pdf");
        document.save(tempFile);
        document.close();

        return tempFile;
    }
}
