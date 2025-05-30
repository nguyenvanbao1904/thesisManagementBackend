/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.MajorDTO;
import com.nvb.services.MajorService;
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
public class MajorValidator implements Validator{
    
    @Autowired
    private MajorService majorService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MajorDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MajorDTO majorDTO = (MajorDTO)target;
        
        // Kiểm tra trùng lặp Username
        if (majorDTO.getName()!= null && !majorDTO.getName().isBlank()) {
            MajorDTO existingMajorByName = majorService.get(Map.of("name", majorDTO.getName()));
            if (majorDTO.getId() == null) {
                if (existingMajorByName != null) {
                    errors.rejectValue("name", "major.name.duplicateMsg");
                }
            } else {
                if (existingMajorByName != null && !existingMajorByName.getId().equals(majorDTO.getId())) {
                    errors.rejectValue("name", "major.name.duplicateMsg");
                }
            }
        }}
    
}
