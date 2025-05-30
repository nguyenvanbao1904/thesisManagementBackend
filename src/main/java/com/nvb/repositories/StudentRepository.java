/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Student;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface StudentRepository {
    Student get(Map<String, String> params, boolean details);
    Student addOrUpdate(Student student);
    List<Student> getAll(Map<String, String> params);
}
