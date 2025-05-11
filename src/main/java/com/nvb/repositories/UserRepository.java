/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.dto.UserDisplayDTO;
import com.nvb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface UserRepository {
    User getUser(Map<String,String> params);
    User addOrUpdateUser(User u);
    boolean authenticate(String username, String password);
    List<UserDisplayDTO> getUsers(Map<String,String> params);
    void deleteUser(User u);
}
