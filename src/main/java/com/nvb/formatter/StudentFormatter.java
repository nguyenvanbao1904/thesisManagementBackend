package com.nvb.formatter;

import com.nvb.pojo.Student;
import com.nvb.repositories.StudentRepository;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class StudentFormatter implements Formatter<Student> {

    @Autowired
    private StudentRepository studentRepository;

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
        Student student = studentRepository.get(Map.of("id", id), true);
        return student;
    }
}
