/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.services.ThesesService;
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
    private ThesesService thesesService; 


    @Override
    public boolean supports(Class<?> clazz) {
        return EvaluationCriteriaDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EvaluationCriteriaDTO ecd = (EvaluationCriteriaDTO) target;
        
        // Kiểm tra trùng lặp name
        if (ecd.getName() != null && !ecd.getName().isBlank()) {
            List<EvaluationCriteriaDTO> listExisting = thesesService.getEvaluationCriterias(new HashMap<>(Map.of("name", ecd.getName())));
            if (ecd.getId() == null) {
                if (!listExisting.isEmpty()) {
                    errors.rejectValue("name", "evaluationCriteria.name.duplicateMsg");
                }
            } else {
                if (!listExisting.isEmpty() && !listExisting.get(0).getId().equals(ecd.getId())) {
                    errors.rejectValue("name", "evaluationCriteria.name.duplicateMsg");
                }
            }
        }
    }
    
}
