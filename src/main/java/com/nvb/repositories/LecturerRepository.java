/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Lecturer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface LecturerRepository {
    Lecturer get(Map<String, String> params);
    List<Lecturer> getAll(Map<String, String> params);
    List<Lecturer> getByIds(List<Integer> ids);
}
