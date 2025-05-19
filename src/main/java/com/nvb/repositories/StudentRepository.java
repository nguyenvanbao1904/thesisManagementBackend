/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Student;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface StudentRepository {
    Student getStudent(Map<String, String> params);
    Student getStudentWithDetails(Map<String, String> params);
}
