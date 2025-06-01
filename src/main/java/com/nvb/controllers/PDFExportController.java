package com.nvb.controllers;

import com.itextpdf.text.DocumentException;
import com.nvb.dto.CommitteeDTO;
import com.nvb.services.CommitteeService;
import com.nvb.services.EvaluationService;
import com.nvb.services.PDFExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/export")
@CrossOrigin
public class PDFExportController {

    @Autowired
    private PDFExportService pdfExportService;

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/committee/{committeeId}/scores")
    public ResponseEntity<byte[]> exportCommitteeScores(@PathVariable Integer committeeId) {
        try {
            // Lấy thông tin hội đồng
            CommitteeDTO committee = committeeService.get(Map.of("id", committeeId.toString()));

            if (committee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Tạo tên file PDF
            String filename = "BangDiemHoiDong_" + committeeId + ".pdf";

            // Tạo PDF với thông tin chi tiết
            byte[] pdfBytes = pdfExportService.generateCommitteeScoreReport(
                committeeId,
                committee.getTheses(),
                committee.getCommitteeMembers(),
                "Hội đồng ID: " + committeeId,
                committee.getLocation(),
                Date.from(committee.getDefenseDate().atZone(ZoneId.systemDefault()).toInstant())
            );

            // Thiết lập HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
