/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.repositories.EvaluationCriteriaRepository;
import com.nvb.services.EvaluationCriteriaService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService{
    
    @Autowired
    private EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params) {
        return this.getEvaluationCriterias(params, false);
    }

    @Override
    public List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params, boolean pagination) {
        return evaluationCriteriaRepository.getEvaluationCriterias(params, pagination);
    }

    @Override
    public EvaluationCriteria getEvaluationCriteria(Map<String, String> params) {
        return evaluationCriteriaRepository.getEvaluationCriteria(params);
    }

    @Override
    public EvaluationCriteria addOrUpdateEvaluationCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) {
        EvaluationCriteria evaluationCriteria = new EvaluationCriteria();
        evaluationCriteria.setName(evaluationCriteriaDTO.getName());
        evaluationCriteria.setDescription(evaluationCriteriaDTO.getDescription());
        evaluationCriteria.setMaxPoint(evaluationCriteriaDTO.getMaxPoint());
        evaluationCriteria.setId(evaluationCriteriaDTO.getId());
        return evaluationCriteriaRepository.addOrUpdateEvaluationCriteria(evaluationCriteria);
    }

    @Override
    public void deleteEvaluationCriteria(int id) {
        evaluationCriteriaRepository.deleteEvaluationCriteria(id);
    }
    
}
