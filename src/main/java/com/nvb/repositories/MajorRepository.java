/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Major;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface MajorRepository {
    Major addOrUpdate(Major major);
    List<Major> getAll(Map<String, String> params);
    Major get(Map<String, String> params);
    void delete(int id);
}
