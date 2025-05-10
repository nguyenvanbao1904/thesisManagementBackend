/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.Lecturer;
import com.nvb.repositories.LecturerRepository;
import com.nvb.services.LecturerService;
import com.nvb.services.UserService;
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
public class LectureServiceImpl implements LecturerService{
    
    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private LecturerRepository lecturerRepository; 

    @Override
    public Lecturer addLecturer(Map<String, String> params) {
        Lecturer lecturer = new Lecturer();
        String academicTitle = params.get("academicTitle");

        lecturer.setAcademicDegree(params.get("academicDegree"));
        lecturer.setAcademicTitle(academicTitle == null || academicTitle.trim().isEmpty() ? null : academicTitle);
        lecturer.setUser(userDetailsService.getUser(Map.of("username", params.get("username"))));
        return lecturerRepository.addLecturer(lecturer);
    }
    
}
