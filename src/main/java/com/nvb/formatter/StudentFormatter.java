package com.nvb.formatter;

import com.nvb.pojo.Student;
import com.nvb.services.StudentService;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class StudentFormatter implements Formatter<Student> {

    @Autowired
    private StudentService studentService;

    @Override
    public String print(Student object, Locale locale) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object.getId());
    }

    @Override
    public Student parse(String id, Locale locale) throws ParseException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        Student student = studentService.getStudentWithDetails(Map.of("id", id));
        return student;
    }
}
