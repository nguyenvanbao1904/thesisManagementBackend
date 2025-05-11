/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Student;
import com.nvb.pojo.User;
import com.nvb.services.MajorService;
import com.nvb.services.StudentService;
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
    private MajorService majorService;

    public Student prepareStudent(User user, UserDTO userDto) {
        Student student = new Student();
        if (userDto.getMajorId() != null) {
            student.setMajor(majorService.getMajorById(userDto.getMajorId()));
        }
        student.setStudentId(userDto.getStudentId());
        student.setUser(user);
        return student;
    }
    
}
