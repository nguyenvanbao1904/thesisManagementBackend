/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.AcademicStaff;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface AcademicsStaffRepository {
    AcademicStaff get(Map<String, String> params);
    List<AcademicStaff> getAll(Map<String, String> params);
}
