/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.EvaluationScoreDTO;
import com.nvb.services.EvaluationService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyenvanbao
 */
@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin
public class ApiEvaluationController {
    
    @Autowired
    private EvaluationService evaluationService;
    
    // cham diem
    @PostMapping("")
    public ResponseEntity<List<EvaluationScoreDTO>> create(Model model, @RequestBody List<EvaluationScoreDTO> evaluationScoreDTOs){
        evaluationService.evaluate(evaluationScoreDTOs);
        return new ResponseEntity<>(evaluationScoreDTOs, HttpStatus.OK);
    }
    
    // lay diem
    @GetMapping("/{thesisId}")
    public ResponseEntity<List<EvaluationScoreDTO>> retrieve(@PathVariable(value = "thesisId") int thesisId,  @RequestParam Map<String, String> params){
        params.put("thesisId", String.valueOf(thesisId));
        return new ResponseEntity<>(evaluationService.getEvaluation(params), HttpStatus.OK);
    }

}

