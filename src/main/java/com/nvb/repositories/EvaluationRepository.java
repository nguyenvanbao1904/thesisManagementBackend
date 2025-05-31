/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.EvaluationScore;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationRepository {
    void addOrUpdateEvaluation(EvaluationScore evaluationScore);
    List<EvaluationScore> getEvaluation(Map<String, String> params);
    Float calculateAverageScore(Integer thesisId);
    void addOrUpdateFinalScore(EvaluationFinalScore finalScore);
    EvaluationFinalScore getFinalScore(Integer thesisId);
}
