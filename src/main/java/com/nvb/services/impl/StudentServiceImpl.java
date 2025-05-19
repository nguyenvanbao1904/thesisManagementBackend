/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Student;
import com.nvb.pojo.User;
import com.nvb.repositories.StudentRepository;
import com.nvb.services.MajorService;
import com.nvb.services.StudentService;
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
    private StudentRepository studentRepository;
    
    @Autowired
    private MajorService majorService;

    public Student prepareStudent(User user, UserDTO userDto) {
        Student student;
        if(user.getStudent() != null){
            student = user.getStudent();
        }else{
            student = new Student();
            student.setUser(user);
        }
        if (userDto.getMajorId() != null) {
            student.setMajor(majorService.getMajorById(userDto.getMajorId()));
        }
        student.setStudentId(userDto.getStudentId());
        return student;
    }

    @Override
    public Student getStudent(Map<String, String> params) {
        return this.studentRepository.getStudent(params);
    }

    @Override
    public Student getStudentWithDetails(Map<String, String> params) {
        return this.studentRepository.getStudentWithDetails(params);
    }
    
}
