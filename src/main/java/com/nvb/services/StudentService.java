/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Student;
import com.nvb.pojo.User;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface StudentService {
    Student prepareStudent(User user, UserDTO userDto);
    Student getStudent(Map<String, String> params);
}
