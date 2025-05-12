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
    Major addOrUpdateMajor(Major major);
    List<Major> getMajors(Map<String, String> params);
    Major getMajorById(int majorId);
    void deleteMajor(int id);
}
