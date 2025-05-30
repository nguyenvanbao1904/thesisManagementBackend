/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.formatter;

import com.nvb.pojo.EvaluationCriteria;
import com.nvb.repositories.EvaluationCriteriaRepository;
import java.text.ParseException;
import java.util.HashMap;
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
public class EvaluationCriteriaFormatter implements Formatter<EvaluationCriteria> {

    @Autowired
    private EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public String print(EvaluationCriteria object, Locale locale) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object.getId());
    }

    @Override
    public EvaluationCriteria parse(String text, Locale locale) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return evaluationCriteriaRepository.get(new HashMap<>(Map.of("id", text)));
    }

}
