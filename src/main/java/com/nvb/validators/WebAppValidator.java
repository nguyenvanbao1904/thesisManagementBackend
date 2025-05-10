/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import jakarta.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author nguyenvanbao
 */
@Component
public class WebAppValidator implements Validator {

    @Autowired
    private jakarta.validation.Validator beanValidator;
    private Set<Validator> springValidators = new HashSet<>();
    private Class<?> supportedClass;

    @Override
    public boolean supports(Class<?> clazz) {
        return supportedClass.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Set<ConstraintViolation<Object>> constraintViolations
                = beanValidator.validate(target);
        for (ConstraintViolation<Object> obj : constraintViolations) {
            errors.rejectValue(obj.getPropertyPath().toString(),
                    obj.getMessageTemplate(), obj.getMessage());
        }
        for (Validator obj : springValidators) {
            obj.validate(target, errors);
        }
    }

    public void setSpringValidators(
            Set<Validator> springValidators) {
        this.springValidators = springValidators;
    }

    public void setSupportedClass(Class<?> supportedClass) {
        this.supportedClass = supportedClass;
    }
}
