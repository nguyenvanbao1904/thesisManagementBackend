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
        Lecturer lecturer = new Lecturer();
        lecturer.setAcademicDegree(userDto.getAcademicDegree());
        String academicTitle = userDto.getAcademicTitle();
        lecturer.setAcademicTitle(academicTitle == null || academicTitle.trim().isEmpty() ? null : academicTitle);
        lecturer.setUser(user);
        return lecturer;
    }
    
}
