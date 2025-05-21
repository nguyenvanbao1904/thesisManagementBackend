/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface LecturerService {
    Lecturer prepareLecturer(User user, UserDTO userDto);
    Lecturer getLecturerWithDetails(Map<String, String> params);
    List<Lecturer> getLecturers(Map<String,String> params);
}
