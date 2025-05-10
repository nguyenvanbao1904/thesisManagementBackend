/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.Student;
import com.nvb.repositories.StudentRepository;
import com.nvb.services.MajorService;
import com.nvb.services.StudentService;
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
public class StudentServiceImpl implements StudentService{
    @Autowired
    private UserService userDetailsService;
    
    @Autowired
    private MajorService majorService;
    
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student addStudent(Map<String, String> params) {
        Student student = new Student();
        student.setMajor(majorService.getMajorById(Integer.parseInt(params.get("majorId"))));
        student.setStudentId(params.get("studentId"));
        student.setUser(userDetailsService.getUser(Map.of("username", params.get("username"))));
        return studentRepository.addStudent(student);
    }
    
    
}
