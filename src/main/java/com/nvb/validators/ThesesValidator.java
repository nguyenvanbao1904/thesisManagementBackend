/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.validators;

import com.nvb.dto.CommitteeDTO;
import com.nvb.dto.ThesesDTO;
import com.nvb.dto.UserDTO;
import com.nvb.services.CommitteeService;
import com.nvb.services.ThesesService;
import com.nvb.services.UserService;
import java.util.HashMap;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CommitteeService committeeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ThesesDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ThesesDTO thesesDTO = (ThesesDTO) target;

        // Kiểm tra trùng lặp title
        if (thesesDTO.getTitle() != null && !thesesDTO.getTitle().isBlank()) {
            ThesesDTO existingThesisByTitle = thesesService.get(Map.of("title", thesesDTO.getTitle()));
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
        if (thesesDTO.getLecturerIds() != null && thesesDTO.getLecturerIds().size() > 2) {
            errors.rejectValue("lecturerIds", "theses.lecturers.maxMsg");
        }
        // tối đa 2 sinh viên tham gia
        if (thesesDTO.getStudentIds() != null && thesesDTO.getStudentIds().size() > 2) {
            errors.rejectValue("studentIds", "theses.students.maxMsg");
        }

        // Tối đa 5 khóa luận cho 1 hội đồng
        if (thesesDTO.getCommitteeId() != null) {
            CommitteeDTO committeeDTO = committeeService.get(new HashMap<>(Map.of("id", thesesDTO.getCommitteeId().toString())));
            if(committeeDTO.getTheses() != null && committeeDTO.getTheses().size() > 5){
                errors.rejectValue("committeeId", "theses.committee.limitMsg");
            }
        }

        // 1 sinh viên chỉ được tham gia 1 khóa luận
        if (thesesDTO.getStudentIds() != null) {
            for (Integer studentUserId : thesesDTO.getStudentIds()) {
                if (thesesService.isStudentInAnotherActiveThesis(studentUserId, thesesDTO.getId())) {
                    // Lấy thông tin user để hiển thị tên
                    UserDTO studentUser = userService.get(Map.of("id", studentUserId.toString()));
                    String studentName = "";
                    if (studentUser != null) {
                        studentName = studentUser.getLastName() + " " + studentUser.getFirstName();
                    } else {
                        studentName = "Sinh viên (ID: " + studentUserId + ")"; // Fallback nếu không lấy được user
                    }
                    errors.rejectValue("studentIds",
                            "theses.student.alreadyRegistered",
                            String.format("Sinh viên %s đã được đăng ký một khóa luận tốt nghiệp khác.", studentName));
                }
            }
        }
    }
}
