/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.User;
import com.nvb.repositories.AcademicsStaffRepository;
import com.nvb.services.AcademicsStaffService;
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
public class AcademicsStaffServiceImpl implements AcademicsStaffService{
    
    @Autowired
    private AcademicsStaffRepository academicsStaffRepository;
    
    @Override
    public AcademicStaff prepareAcademicStaff(User user, UserDTO userDto) {
        AcademicStaff staff = new AcademicStaff();
        staff.setUser(user);
        return staff;
    }
}
