/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.formatter;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.services.ThesesService;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 *
 * @author nguyenvanbao
 */
@Component
public class EvaluationCriteriaDTOFormatter implements Formatter<EvaluationCriteriaDTO>{
    
    @Autowired
    private ThesesService thesesService;

    @Override
    public String print(EvaluationCriteriaDTO object, Locale locale) {
         if (object == null) {
            return "";
        }
        return String.valueOf(object.getId());
    }

    @Override
    public EvaluationCriteriaDTO parse(String text, Locale locale) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null; // Hoặc throw ParseException nếu ID trống không được phép
        }
        
        return thesesService.getEvaluationCriterias(Map.of("id", text)).get(0);
    }
    
}
