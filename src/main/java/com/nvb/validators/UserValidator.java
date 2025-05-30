package com.nvb.validators;

import com.nvb.dto.UserDTO;
import com.nvb.services.UserService;
import com.nvb.repositories.StudentRepository;
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
    private UserService userService;
    
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDto = (UserDTO) target;

        // Kiểm tra trùng lặp Username
        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            UserDTO existingUserByUsername = userService.get(Map.of("username", userDto.getUsername()));
            if (userDto.getId() == null) {
                if (existingUserByUsername != null) {
                    errors.rejectValue("username", "user.username.duplicateMsg");
                }
            } else {
                if (existingUserByUsername != null && !existingUserByUsername.getId().equals(userDto.getId())) {
                    errors.rejectValue("username", "user.username.duplicateMsg");
                }
            }
        }

        // Kiểm tra trùng lặp Email
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            UserDTO existingUserByEmail = userService.get(Map.of("email", userDto.getEmail()));
            if (userDto.getId() == null) { 
                if (existingUserByEmail != null) {
                    errors.rejectValue("email", "user.email.duplicateMsg");
                }
            } else {
                if (existingUserByEmail != null && !existingUserByEmail.getId().equals(userDto.getId())) {
                    errors.rejectValue("email", "user.email.duplicateMsg");
                }
            }
        }

        // Kiểm tra trùng lặp Phone
        if (userDto.getPhone() != null && !userDto.getPhone().isBlank()) {
            if (!userDto.getPhone().matches("^(84|0[3|5|7|8|9])[0-9]{8}$")) {
                errors.rejectValue("phone", "user.phone.invalidMsg");
            } else {
                UserDTO existingUserByPhone = userService.get(Map.of("phone", userDto.getPhone()));
                if (userDto.getId() == null) {
                    if (existingUserByPhone != null) {
                        errors.rejectValue("phone", "user.phone.duplicateMsg");
                    }
                } else {
                    if (existingUserByPhone != null && !existingUserByPhone.getId().equals(userDto.getId())) {
                        errors.rejectValue("phone", "user.phone.duplicateMsg");
                    }
                }
            }
        }
        
        // === VALIDATION MẬT KHẨU ===
        if (userDto.getId() == null) {
            if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
                errors.rejectValue("password", "user.password.notnullMsg");
            } else if (userDto.getPassword().length() < 6) {
                errors.rejectValue("password", "user.password.tooshortMsg");
            }
        } else {
            if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
                if (userDto.getPassword().length() < 6) {
                    errors.rejectValue("password", "user.password.tooshortMsg");
                }
            }
        }

        if ("ROLE_STUDENT".equals(userDto.getRole())) {
            if (userDto.getStudentId() == null || userDto.getStudentId().isBlank()) { 
                errors.rejectValue("studentId", "user.studentId.notnullMsg");
            } else if (!userDto.getStudentId().matches("^[A-Z0-9]{6,20}$")) {
                errors.rejectValue("studentId", "user.studentId.invalidMsg");
            } else {
                var existingStudent = studentRepository.get(Map.of("studentId", userDto.getStudentId()), false);
                if (existingStudent != null) {
                    if (userDto.getId() == null || !existingStudent.getId().equals(userDto.getId())) {
                        errors.rejectValue("studentId", "user.studentId.duplicateMsg");
                    }
                }
            }

            if (userDto.getMajorId() == null) {
                errors.rejectValue("majorId", "user.majorId.notnullMsg");
            }
        } else if ("ROLE_LECTURER".equals(userDto.getRole())) {
            if (userDto.getAcademicTitle() != null && !userDto.getAcademicTitle().isBlank()) {
                if (!userDto.getAcademicTitle().equals("ASSOCIATE_PROFESSOR")
                        && !userDto.getAcademicTitle().equals("PROFESSOR")) {
                    errors.rejectValue("academicTitle", "user.lecturerTitle.invalidMsg");
                }
            }

            if (userDto.getAcademicDegree() != null && !userDto.getAcademicDegree().isBlank()) {
                if (!userDto.getAcademicDegree().equals("MASTER")
                        && !userDto.getAcademicDegree().equals("DOCTOR")) {
                    errors.rejectValue("academicDegree", "user.lecturerDegree.invalidMsg");
                }
            }
        }
    }
}
