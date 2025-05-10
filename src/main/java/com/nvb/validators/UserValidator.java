package com.nvb.validators;

import com.nvb.dto.UserDTO;
import com.nvb.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author nguyenvanbao
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UserService userDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;
        
        if(userDetailsService.getUser(Map.of("username", user.getUsername())) != null){
            errors.rejectValue("username", "user.username.duplicateMsg");
        }
        
        if(userDetailsService.getUser(Map.of("email", user.getEmail())) != null){
            errors.rejectValue("email", "user.email.duplicateMsg");
        }
        
        if(userDetailsService.getUser(Map.of("phone", user.getPhone())) != null){
            errors.rejectValue("phone", "user.phone.duplicateMsg");
        }
        
        if (!user.getPhone().matches("^(84|0[3|5|7|8|9])[0-9]{8}$")) {
            errors.rejectValue("phone", "user.phone.invalidMsg");
        }

        if ("ROLE_STUDENT".equals(user.getRole())) {
            // Trường bắt buộc cho sinh viên

            if (user.getStudentId() == null || user.getStudentId().isEmpty()) {
                errors.rejectValue("studentId", "user.studentId.notnullMsg");
            } else if (!user.getStudentId().matches("^[A-Z0-9]{6,20}$")) {
                errors.rejectValue("studentId", "user.studentId.invalidMsg");
            }

            if (user.getMajorId() == null) {
                errors.rejectValue("majorId", "user.majorId.notnullMsg");
            }

        } else if ("ROLE_LECTURER".equals(user.getRole())) {
            // Validation cho giảng viên

            if (user.getAcademicTitle() != null && !user.getAcademicTitle().isEmpty()) {
                if (!user.getAcademicTitle().equals("ASSOCIATE_PROFESSOR")
                        && !user.getAcademicTitle().equals("PROFESSOR")) {
                    errors.rejectValue("academicTitle", "user.lecturerTitle.invalidMsg");
                }
            }

            if (user.getAcademicDegree() != null && !user.getAcademicDegree().isEmpty()) {
                if (!user.getAcademicDegree().equals("MASTER")
                        && !user.getAcademicDegree().equals("DOCTOR")) {
                    errors.rejectValue("academicDegree", "user.lecturerDegree.invalidMsg");
                }
            }
        }
    }
}
