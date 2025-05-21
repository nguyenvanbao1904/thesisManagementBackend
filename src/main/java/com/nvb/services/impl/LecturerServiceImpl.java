/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.User;
import com.nvb.repositories.LecturerRepository;
import com.nvb.services.LecturerService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class LecturerServiceImpl implements LecturerService{
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    @Override
    public Lecturer prepareLecturer(User user, UserDTO userDto) {
        Lecturer lecturer;
        if (user.getLecturer() != null) { 
            lecturer = user.getLecturer();
        } else {
            lecturer = new Lecturer();
            lecturer.setUser(user); 
        }
        lecturer.setAcademicTitle(userDto.getAcademicTitle().isBlank() ? null : userDto.getAcademicTitle());
        lecturer.setAcademicDegree(userDto.getAcademicDegree());
        return lecturer;
    }

    @Override
    public Lecturer getLecturerWithDetails(Map<String, String> params) {
        return lecturerRepository.getLecturerWithDetails(params);
    }

    @Override
    public List<Lecturer> getLecturers(Map<String, String> params) {
        return lecturerRepository.getLecturers(params);
    }
}
