/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import com.nvb.dto.UserDTO;

/**
 *
 * @author nguyenvanbao
 */
public interface UserService extends UserDetailsService{
    User getUser(Map<String, String> params);
    User addOrUpdateUser(UserDTO userDto, MultipartFile avatar);
    boolean authenticate(String userName, String password);
    List<User> getUsers(Map<String, String> params);
    void deleteUser(int id);
}
