package com.nvb.formatter;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.services.EvaluationCriteriaCollectionService;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nguyenvanbao
 */
@Component
public class EvaluationCriteriaCollectionFormatter implements Formatter<EvaluationCriteriaCollectionDTO>{
    
    @Autowired
    private EvaluationCriteriaCollectionService evaluationCriteriaCollectionService;
    
    @Override
    public String print(EvaluationCriteriaCollectionDTO object, Locale locale) {
        if(object == null){
            return "";
        }
        return String.valueOf(object.getId());
    }

    @Override
    public EvaluationCriteriaCollectionDTO parse(String text, Locale locale) throws ParseException {
        if(text == null || text.trim().isEmpty()){
            return null;
        }
        return evaluationCriteriaCollectionService.get(new HashMap<>(Map.of("id", text)));
    }
    
}