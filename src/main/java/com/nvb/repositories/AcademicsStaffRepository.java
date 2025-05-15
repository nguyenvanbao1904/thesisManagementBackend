/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.AcademicStaff;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface AcademicsStaffRepository {
    AcademicStaff getAcademicStaff(Map<String, String> params);
}
