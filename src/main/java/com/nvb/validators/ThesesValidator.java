/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.ThesesDTO;
import com.nvb.pojo.Thesis;
import com.nvb.services.ThesesService;
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
public class ThesesValidator implements Validator {
    
    @Autowired
    private ThesesService thesesService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ThesesDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ThesesDTO thesesDTO = (ThesesDTO) target;

        // Kiểm tra trùng lặp title
        if (thesesDTO.getTitle()!= null && !thesesDTO.getTitle().isBlank()) {
            Thesis existingThesisByTitle = thesesService.getThesis(Map.of("title", thesesDTO.getTitle()));
            if (thesesDTO.getId() == null) {
                if (existingThesisByTitle != null) {
                    errors.rejectValue("title", "theses.title.duplicateMsg");
                }
            } else {
                if (existingThesisByTitle != null && !existingThesisByTitle.getId().equals(thesesDTO.getId())) {
                    errors.rejectValue("title", "theses.title.duplicateMsg");
                }
            }
        }
        // tối đa 2 giảng viên hướng dẫn
        if(thesesDTO.getLecturers() != null && thesesDTO.getLecturers().size() > 2){
            errors.rejectValue("lecturers", "theses.lecturers.maxMsg");
        }
        // toi da 2 sinh vien tham gia
        if(thesesDTO.getStudents()!= null && thesesDTO.getStudents().size() > 2){
            errors.rejectValue("students", "theses.students.maxMsg");
        }
    }

}
