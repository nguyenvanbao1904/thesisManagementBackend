/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.User;
import java.util.Map;
/**
 *
 * @author nguyenvanbao
 */
public interface AcademicsStaffService {
    AcademicStaff prepareAcademicStaff(User user, UserDTO userDto);
    AcademicStaff getAcademicStaff(Map<String, String> params);
}
