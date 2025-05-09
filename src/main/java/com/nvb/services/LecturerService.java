/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.services;

import com.nvb.pojo.Lecturer;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface LecturerService {
    Lecturer addLecturer(Map<String, String> params);
}
