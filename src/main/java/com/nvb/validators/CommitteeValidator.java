/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.CommitteeDTO;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author nguyenvanbao
 */
@Component
public class CommitteeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CommitteeDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommitteeDTO committeeDTO = (CommitteeDTO) target;

        int committeeMemberSize = (int) Arrays.stream(committeeDTO.getMemberLecturerId())
                .filter(Objects::nonNull)
                .count();

        if (committeeMemberSize < 3) {
            errors.rejectValue("committeeMembers", "committee.committeeMembers.min");
        }
 
        if (committeeMemberSize > 5) {
            errors.rejectValue("committeeMembers", "committee.committeeMembers.max");
        }
        
        if(committeeDTO.getThesesIds() != null && committeeDTO.getThesesIds().length > 5){
            errors.rejectValue("theses", "committee.theses.max");
        }
    }

}
