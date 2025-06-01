package com.nvb.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.nvb.dto.CommitteeMemberDTO;
import com.nvb.dto.EvaluationScoreDTO;
import com.nvb.dto.ThesesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PDFExportService {

    @Autowired
    private EvaluationService evaluationService;
    
    public byte[] generateCommitteeScoreReport(Integer committeeId, List<ThesesDTO> theses, 
            List<CommitteeMemberDTO> members, String committeeName, String location, Date defenseDate) 
            throws DocumentException, IOException {
        
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Create standard fonts
        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Font subtitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Font smallBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
        
        // University header
        Paragraph header1 = new Paragraph("TRUONG DAI HOC MO TP.HCM", headerFont);
        header1.setAlignment(Element.ALIGN_CENTER);
        document.add(header1);
        
        Paragraph header2 = new Paragraph("HOI DONG KHOA LUAN TOT NGHIEP", headerFont);
        header2.setAlignment(Element.ALIGN_CENTER);
        document.add(header2);
        
        document.add(Chunk.NEWLINE);
        
        // Report title
        Paragraph title = new Paragraph("BANG TONG HOP DIEM KHOA LUAN TOT NGHIEP", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        // Committee info
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String defenseDateStr = defenseDate != null ? dateFormat.format(defenseDate) : "N/A";
        
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Hoi dong: " + committeeName, normalFont));
        document.add(new Paragraph("Dia diem: " + location, normalFont));
        document.add(new Paragraph("Ngay bao ve: " + defenseDateStr, normalFont));
        document.add(Chunk.NEWLINE);
        
        // Process each thesis
        int thesisIndex = 1;
        for (ThesesDTO thesis : theses) {
            // Thesis header
            Paragraph thesisTitle = new Paragraph(thesisIndex + ". " + thesis.getTitle(), subtitleFont);
            document.add(thesisTitle);
            
            // Student info
            StringBuilder studentNames = new StringBuilder("Sinh vien thuc hien: ");
            if (thesis.getStudents() != null) {
                thesis.getStudents().forEach(student -> {
                    if (studentNames.length() > 20) {
                        studentNames.append(", ");
                    }
                    studentNames.append(student.getLastName()).append(" ").append(student.getFirstName());
                });
            }
            document.add(new Paragraph(studentNames.toString(), normalFont));
            
            // Get detailed scores for this thesis
            Map<String, String> scoreParams = new HashMap<>();
            scoreParams.put("thesisId", thesis.getId().toString());
            List<EvaluationScoreDTO> scores = evaluationService.getEvaluation(scoreParams);
            
            if (scores != null && !scores.isEmpty()) {
                // Create table for detailed scores
                PdfPTable detailTable = new PdfPTable(4);
                detailTable.setWidthPercentage(100);
                detailTable.setSpacingBefore(10);
                detailTable.setSpacingAfter(10);
                try {
                    detailTable.setWidths(new float[]{3, 5, 1.5f, 3});
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                
                // Add table header
                PdfPCell headerCell;
                
                headerCell = new PdfPCell(new Phrase("Thanh vien", smallBoldFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setPadding(5);
                headerCell.setBackgroundColor(new BaseColor(220, 220, 220));
                detailTable.addCell(headerCell);
                
                headerCell = new PdfPCell(new Phrase("Tieu chi danh gia", smallBoldFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setPadding(5);
                headerCell.setBackgroundColor(new BaseColor(220, 220, 220));
                detailTable.addCell(headerCell);
                
                headerCell = new PdfPCell(new Phrase("Diem", smallBoldFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setPadding(5);
                headerCell.setBackgroundColor(new BaseColor(220, 220, 220));
                detailTable.addCell(headerCell);
                
                headerCell = new PdfPCell(new Phrase("Nhan xet", smallBoldFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setPadding(5);
                headerCell.setBackgroundColor(new BaseColor(220, 220, 220));
                detailTable.addCell(headerCell);
                
                // Group scores by lecturer
                Map<Integer, Map<String, Object>> lecturerScoresMap = evaluationService.groupScoresByLecturer(scores);
                
                // Add scores to table
                for (Map.Entry<Integer, Map<String, Object>> entry : lecturerScoresMap.entrySet()) {
                    String lecturerName = (String) entry.getValue().get("lecturerName");
                    String role = (String) entry.getValue().get("role");
                    List<EvaluationScoreDTO> lecturerScores = (List<EvaluationScoreDTO>) entry.getValue().get("scores");
                    
                    // First row with lecturer name and first criteria
                    if (!lecturerScores.isEmpty()) {
                        PdfPCell cell;
                        
                        // Lecturer name with role
                        cell = new PdfPCell(new Phrase(lecturerName + " (" + role + ")", smallFont));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(5);
                        cell.setRowspan(lecturerScores.size());
                        detailTable.addCell(cell);
                        
                        // First criteria
                        addScoreRow(detailTable, lecturerScores.get(0), smallFont);
                        
                        // Remaining criteria
                        for (int i = 1; i < lecturerScores.size(); i++) {
                            addScoreRow(detailTable, lecturerScores.get(i), smallFont);
                        }
                    }
                }
                
                document.add(detailTable);
                
                // Average score
                String scoreText = thesis.getAverageScore() != null ? 
                        String.format("Diem trung binh: %.2f", thesis.getAverageScore()) : 
                        "Diem trung binh: Chua co";
                Paragraph avgScore = new Paragraph(scoreText, boldFont);
                avgScore.setAlignment(Element.ALIGN_RIGHT);
                document.add(avgScore);
            } else {
                document.add(new Paragraph("Chua co du lieu danh gia cho khoa luan nay.", normalFont));
            }
            
            document.add(Chunk.NEWLINE);
            thesisIndex++;
        }
        
        // Add signature sections
        document.add(Chunk.NEWLINE);
        
        PdfPTable signatureTable = new PdfPTable(3);
        signatureTable.setWidthPercentage(100);
        
        // Current date
        SimpleDateFormat dateSigFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateSigFormat.format(new Date());
        
        // First column - empty space
        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setBorder(Rectangle.NO_BORDER);
        signatureTable.addCell(emptyCell);
        
        // Second column - empty space
        signatureTable.addCell(emptyCell);
        
        // Third column - signature space
        PdfPCell signatureCell = new PdfPCell();
        signatureCell.setBorder(Rectangle.NO_BORDER);
        Paragraph signaturePara = new Paragraph();
        signaturePara.add(new Paragraph("TP.HCM, ngay " + currentDate, normalFont));
        signaturePara.add(new Paragraph("CHU TICH HOI DONG", boldFont));
        signaturePara.setAlignment(Element.ALIGN_CENTER);
        signatureCell.addElement(signaturePara);
        signatureTable.addCell(signatureCell);
        
        document.add(signatureTable);
        
        document.close();
        return baos.toByteArray();
    }
    
    private void addScoreRow(PdfPTable table, EvaluationScoreDTO score, Font font) {
        PdfPCell cell;
        
        // Criteria name
        cell = new PdfPCell(new Phrase(score.getCriteriaName(), font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
        
        // Score
        cell = new PdfPCell(new Phrase(String.format("%.1f", score.getScore()), font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
        
        // Comment
        cell = new PdfPCell(new Phrase(score.getComment() != null ? score.getComment() : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }
}