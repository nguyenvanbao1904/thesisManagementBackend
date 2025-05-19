package com.nvb.formatter;

import com.nvb.pojo.Student;
import com.nvb.pojo.User;
import com.nvb.services.UserService;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class StudentFormatter implements Formatter<Student> {

    @Autowired
    private UserService userDetailsService;

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
        User user = userDetailsService.getUser(Map.of("id", id));
        if (user != null && user.getStudent() != null) {
            if (user.getStudent().getId().equals(Integer.parseInt(id))) {
                return user.getStudent();
            }
        }
        return null;
    }
}
