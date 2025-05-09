/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.pojo.Major;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface MajorService {
    Major addMajor(Major major);
    List<Major> getMajors(Map<String, String> params);
}
