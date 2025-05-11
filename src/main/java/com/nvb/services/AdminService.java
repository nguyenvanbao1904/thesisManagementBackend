/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Admin;
import com.nvb.pojo.User;
/**
 *
 * @author nguyenvanbao
 */
public interface AdminService {
    Admin prepareAdmin(User user, UserDTO userDto);
}
