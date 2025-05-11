package com.nvb.validators;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Student;
import com.nvb.pojo.User;
import com.nvb.services.StudentService;
import com.nvb.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author nguyenvanbao
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private StudentService studentService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;

        // Kiểm tra trùng lặp Username
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            User existingUserByUsername = userDetailsService.getUser(Map.of("username", user.getUsername()));
            if (user.getId() == null) {
                if (existingUserByUsername != null) {
                    errors.rejectValue("username", "user.username.duplicateMsg");
                }
            } else {
                if (existingUserByUsername != null && !existingUserByUsername.getId().equals(user.getId())) {
                    errors.rejectValue("username", "user.username.duplicateMsg");
                }
            }
        }

        // Kiểm tra trùng lặp Email
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            User existingUserByEmail = userDetailsService.getUser(Map.of("email", user.getEmail()));
            if (user.getId() == null) { 
                if (existingUserByEmail != null) {
                    errors.rejectValue("email", "user.email.duplicateMsg");
                }
            } else {
                if (existingUserByEmail != null && !existingUserByEmail.getId().equals(user.getId())) {
                    errors.rejectValue("email", "user.email.duplicateMsg");
                }
            }
        }

        // Kiểm tra trùng lặp Phone
        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            if (!user.getPhone().matches("^(84|0[3|5|7|8|9])[0-9]{8}$")) {
                errors.rejectValue("phone", "user.phone.invalidMsg");
            } else {
                User existingUserByPhone = userDetailsService.getUser(Map.of("phone", user.getPhone()));
                if (user.getId() == null) {
                    if (existingUserByPhone != null) {
                        errors.rejectValue("phone", "user.phone.duplicateMsg");
                    }
                } else {
                    if (existingUserByPhone != null && !existingUserByPhone.getId().equals(user.getId())) {
                        errors.rejectValue("phone", "user.phone.duplicateMsg");
                    }
                }
            }
        }
        
        // === VALIDATION MẬT KHẨU ===
        if (user.getId() == null) {
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                errors.rejectValue("password", "user.password.notnullMsg");
            } else if (user.getPassword().length() < 6) {
                errors.rejectValue("password", "user.password.tooshortMsg");
            }
        } else {
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                if (user.getPassword().length() < 6) {
                    errors.rejectValue("password", "user.password.tooshortMsg");
                }
            }
        }

        if ("ROLE_STUDENT".equals(user.getRole())) {
            if (user.getStudentId() == null || user.getStudentId().isBlank()) { 
                errors.rejectValue("studentId", "user.studentId.notnullMsg");
            } else if (!user.getStudentId().matches("^[A-Z0-9]{6,20}$")) {
                errors.rejectValue("studentId", "user.studentId.invalidMsg");
            }else {
                Student existingStudentByStudentId = studentService.getStudent(Map.of("studentId", user.getStudentId()));

                if (user.getId() == null) {
                    if (existingStudentByStudentId != null) {
                        errors.rejectValue("studentId", "user.studentId.duplicateMsg");
                    }
                } else {
                    if (existingStudentByStudentId != null && !existingStudentByStudentId.getId().equals(user.getId())) {
                        errors.rejectValue("studentId", "user.studentId.duplicateMsg");
                    }
                }
            }

            if (user.getMajorId() == null) {
                errors.rejectValue("majorId", "user.majorId.notnullMsg");
            }

        } else if ("ROLE_LECTURER".equals(user.getRole())) {
            if (user.getAcademicTitle() != null && !user.getAcademicTitle().isBlank()) {
                if (!user.getAcademicTitle().equals("ASSOCIATE_PROFESSOR")
                        && !user.getAcademicTitle().equals("PROFESSOR")) {
                    errors.rejectValue("academicTitle", "user.lecturerTitle.invalidMsg");
                }
            }

            if (user.getAcademicDegree() != null && !user.getAcademicDegree().isBlank()) {
                if (!user.getAcademicDegree().equals("MASTER")
                        && !user.getAcademicDegree().equals("DOCTOR")) {
                    errors.rejectValue("academicDegree", "user.lecturerDegree.invalidMsg");
                }
            }
        }
    }
}
