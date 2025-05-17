/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.services.EvaluationCriteriaService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author nguyenvanbao
 */
@Component
public class EvaluationCriteriaValidator implements Validator{
    
    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService; 


    @Override
    public boolean supports(Class<?> clazz) {
        return EvaluationCriteriaDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EvaluationCriteriaDTO ecd = (EvaluationCriteriaDTO) target;
        
        // Kiểm tra trùng lặp name
        if (ecd.getName() != null && !ecd.getName().isBlank()) {
            EvaluationCriteria existing = evaluationCriteriaService.getEvaluationCriteria(new HashMap<>(Map.of("name", ecd.getName())));
            if (ecd.getId() == null) {
                if (existing != null) {
                    errors.rejectValue("name", "evaluationCriteria.name.duplicateMsg");
                }
            } else {
                if (existing != null && existing.getId().equals(ecd.getId())) {
                    errors.rejectValue("name", "evaluationCriteria.name.duplicateMsg");
                }
            }
        }
    }
    
}
