/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface UserRepository {
    User get(Map<String,String> params);
    User addOrUpdate(User u);
    User authenticate(String username, String password);
    List<User> getAll(Map<String,String> params, boolean pagination, boolean details);
    void delete(User u);
    List<User> getByIds(List<Integer> ids);
}
