package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.modules.finance.contributions.models.CollectionPaymentSummaryResponse;
import com.schoolmoney.pl.modules.finance.contributions.models.StudentPaymentSummary;
import com.schoolmoney.pl.modules.finance.contributions.services.CollectionPaymentSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionReportService {
    private final CollectionPaymentSummaryService paymentSummaryService;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public byte[] generatePdfReport(UUID collectionId) {
        log.info("Generating PDF report for collection: {}", collectionId);

        CollectionPaymentSummaryResponse summary = paymentSummaryService.getPaymentSummary(collectionId);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float y = yStart;

                // Title
                cs.beginText();
                cs.setFont(fontBold, 18);
                cs.newLineAtOffset(margin, y);
                cs.showText("Collection Report");
                cs.endText();
                y -= 30;

                // Collection info
                cs.beginText();
                cs.setFont(fontBold, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("Title: " + sanitize(summary.collectionTitle()));
                cs.endText();
                y -= 18;

                if (summary.description() != null && !summary.description().isEmpty()) {
                    cs.beginText();
                    cs.setFont(fontRegular, 10);
                    cs.newLineAtOffset(margin, y);
                    String desc = summary.description().length() > 100
                            ? summary.description().substring(0, 100) + "..."
                            : summary.description();
                    cs.showText("Description: " + sanitize(desc));
                    cs.endText();
                    y -= 16;
                }

                cs.beginText();
                cs.setFont(fontRegular, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText("Status: " + summary.status());
                cs.endText();
                y -= 16;

                if (summary.deadline() != null) {
                    cs.beginText();
                    cs.setFont(fontRegular, 10);
                    cs.newLineAtOffset(margin, y);
                    cs.showText("Deadline: " + DATE_FMT.format(summary.deadline()));
                    cs.endText();
                    y -= 16;
                }

                cs.beginText();
                cs.setFont(fontRegular, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText(String.format("Goal: %d PLN | Collected: %.2f PLN | Remaining: %.2f PLN",
                        summary.totalGoal(), summary.totalCollected(), summary.remainingAmount()));
                cs.endText();
                y -= 16;

                cs.beginText();
                cs.setFont(fontRegular, 10);
                cs.newLineAtOffset(margin, y);
                cs.showText(String.format("Students: %d total | %d paid in full",
                        summary.totalStudents(), summary.studentsPaidInFull()));
                cs.endText();
                y -= 16;

                if (summary.perStudentGoal() != null) {
                    cs.beginText();
                    cs.setFont(fontRegular, 10);
                    cs.newLineAtOffset(margin, y);
                    cs.showText(String.format("Per-student goal: %.2f PLN", summary.perStudentGoal()));
                    cs.endText();
                    y -= 16;
                }

                y -= 20;

                // Table header
                float[] colWidths = {30, 150, 80, 80, 80, 75};
                String[] headers = {"#", "Student", "Goal", "Paid", "Remaining", "Status"};

                // Draw header background
                cs.setNonStrokingColor(0.9f, 0.9f, 0.9f);
                cs.addRect(margin, y - 15, tableWidth, 18);
                cs.fill();
                cs.setNonStrokingColor(0, 0, 0);

                float xPos = margin + 5;
                cs.beginText();
                cs.setFont(fontBold, 9);
                for (int i = 0; i < headers.length; i++) {
                    cs.newLineAtOffset(i == 0 ? xPos : colWidths[i], 0);
                    if (i == 0) cs.newLineAtOffset(0, y - 10);
                    cs.showText(headers[i]);
                }
                cs.endText();

                y -= 20;

                // Table rows
                if (summary.students() != null) {
                    int rowNum = 1;
                    for (StudentPaymentSummary student : summary.students()) {
                        if (y < margin + 30) {
                            // New page
                            cs.close();
                            PDPage newPage = new PDPage(PDRectangle.A4);
                            document.addPage(newPage);
                            PDPageContentStream newCs = new PDPageContentStream(document, newPage);
                            y = newPage.getMediaBox().getHeight() - margin;
                            writeStudentRow(newCs, fontRegular, margin, y, colWidths, rowNum, student);
                            y -= 16;
                            rowNum++;
                            newCs.close();
                            continue;
                        }

                        writeStudentRow(cs, fontRegular, margin, y, colWidths, rowNum, student);
                        y -= 16;
                        rowNum++;
                    }
                }

                // Footer line
                y -= 10;
                cs.setStrokingColor(0.7f, 0.7f, 0.7f);
                cs.moveTo(margin, y);
                cs.lineTo(margin + tableWidth, y);
                cs.stroke();
                y -= 20;

                cs.beginText();
                cs.setFont(fontRegular, 8);
                cs.newLineAtOffset(margin, y);
                cs.showText("Generated by MoneySchool - " + DATE_FMT.format(java.time.Instant.now()));
                cs.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("Error generating PDF report", e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void writeStudentRow(PDPageContentStream cs, PDType1Font font,
                                  float margin, float y, float[] colWidths,
                                  int rowNum, StudentPaymentSummary student) throws IOException {
        cs.beginText();
        cs.setFont(font, 9);
        float xPos = margin + 5;
        cs.newLineAtOffset(xPos, y);
        cs.showText(String.valueOf(rowNum));

        cs.newLineAtOffset(colWidths[1], 0);
        String name = sanitize(student.studentFirstName() + " " + student.studentLastName());
        if (name.length() > 25) name = name.substring(0, 25) + "..";
        cs.showText(name);

        cs.newLineAtOffset(colWidths[2], 0);
        cs.showText(String.format("%.2f PLN", student.perStudentGoal()));

        cs.newLineAtOffset(colWidths[3], 0);
        cs.showText(String.format("%.2f PLN", student.amountPaid()));

        cs.newLineAtOffset(colWidths[4], 0);
        cs.showText(String.format("%.2f PLN", student.remainingAmount()));

        cs.newLineAtOffset(colWidths[5], 0);
        cs.showText(student.isPaidInFull() ? "PAID" : "PARTIAL");

        cs.endText();
    }

    private String sanitize(String text) {
        if (text == null) return "";
        // Replace characters that PDType1Font can't encode
        return text.replaceAll("[^\\x20-\\x7E]", "?");
    }
}
