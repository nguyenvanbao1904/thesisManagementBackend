package com.nvb.validators;

import com.nvb.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;

        if ("ROLE_STUDENT".equals(user.getRole())) {
            // Trường bắt buộc cho sinh viên

            if (user.getStudentId() == null || user.getStudentId().isEmpty()) {
                // Lấy message từ MessageSource, nếu không thấy thì dùng "Student ID is required (default)" làm fallback
                String message = messageSource.getMessage("user.studentId.notnullMsg", null, "Student ID is required (default fallback from validator)", LocaleContextHolder.getLocale());
                // Sử dụng rejectValue với message đã được lấy, nhưng vẫn giữ code gốc cho trường hợp th:errors muốn dùng code
                errors.rejectValue("studentId", "user.studentId.notnullMsg", message);
            } else if (!user.getStudentId().matches("^[A-Z0-9]{6,20}$")) {
                String message = messageSource.getMessage("user.studentId.invalidMsg", null, "Student ID format is invalid (default fallback from validator)", LocaleContextHolder.getLocale());
                errors.rejectValue("studentId", "user.studentId.invalidMsg", message);
            }

            if (user.getMajorId() == null) {
                String message = messageSource.getMessage("user.majorId.notnullMsg", null, "Major is required for students (default fallback from validator)", LocaleContextHolder.getLocale());
                errors.rejectValue("majorId", "user.majorId.notnullMsg", message);
            }

        } else if ("ROLE_LECTURER".equals(user.getRole())) {
            // Validation cho giảng viên

            if (user.getAcademicTitle() != null && !user.getAcademicTitle().isEmpty()) {
                if (!user.getAcademicTitle().equals("ASSOCIATE_PROFESSOR")
                        && !user.getAcademicTitle().equals("PROFESSOR")) {
                    String message = messageSource.getMessage("user.lecturerTitle.invalidMsg", null, "Invalid academic title (default fallback from validator)", LocaleContextHolder.getLocale());
                    errors.rejectValue("academicTitle", "user.lecturerTitle.invalidMsg", message);
                }
            }

            if (user.getAcademicDegree() != null && !user.getAcademicDegree().isEmpty()) {
                if (!user.getAcademicDegree().equals("MASTER")
                        && !user.getAcademicDegree().equals("DOCTOR")) {
                    String message = messageSource.getMessage("user.lecturerDegree.invalidMsg", null, "Invalid academic degree (default fallback from validator)", LocaleContextHolder.getLocale());
                    errors.rejectValue("academicDegree", "user.lecturerDegree.invalidMsg", message);
                }
            }
        }
    }
}
