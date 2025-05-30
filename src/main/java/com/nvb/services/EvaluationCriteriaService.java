/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.EvaluationCriteriaDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface EvaluationCriteriaService {

    List<EvaluationCriteriaDTO> getAll(Map<String, String> params);

    List<EvaluationCriteriaDTO> getAll(Map<String, String> params, boolean pagination);

    EvaluationCriteriaDTO get(Map<String, String> params);
    
    EvaluationCriteriaDTO addOrUpdate(EvaluationCriteriaDTO evaluationCriteriaDTO);
    
    void delete(int id);
}
