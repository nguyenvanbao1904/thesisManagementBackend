/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvb.repositories;

import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import com.nvb.pojo.Thesis;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguyenvanbao
 */
public interface ThesesRepository {
    List<Thesis> getTheses(Map<String, String> params, boolean pagination);
    Thesis addOrUpdate(Thesis thesis);
    Thesis getThesis(Map<String, String> params);
    Lecturer getLecturerWithTheses(Integer lecturerId);
    Student getStudentWithTheses(Integer studentId);
    void deleteThesis(int id);
}
