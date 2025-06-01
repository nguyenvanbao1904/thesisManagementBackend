/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.EvaluationFinalScoreDTO;
import com.nvb.dto.EvaluationScoreDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationService {
    void evaluate(List<EvaluationScoreDTO> evaluationScoreDTOs);
    List<EvaluationScoreDTO> getEvaluation(Map<String, String> params);
    EvaluationFinalScoreDTO getFinalScore(Integer thesisId);
    Map<Integer, Map<String, Object>> groupScoresByLecturer(List<EvaluationScoreDTO> scores);
}
