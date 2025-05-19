package com.nvb.formatter;

import com.nvb.pojo.Lecturer;
import com.nvb.pojo.User;
import com.nvb.services.UserService;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class LecturerFormatter implements Formatter<Lecturer> {

    @Autowired
    private UserService userDetailsService;

    @Override
    public String print(Lecturer object, Locale locale) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object.getId());
    }

    @Override
    public Lecturer parse(String id, Locale locale) throws ParseException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        User user = userDetailsService.getUser(Map.of("id", id));
        if (user != null && user.getLecturer() != null) {
            // Ensure the ID of the fetched lecturer matches the input ID, 
            // as user.getLecturer().getId() should be the same as user.getId()
            if (user.getLecturer().getId().equals(Integer.parseInt(id))) {
                return user.getLecturer();
            }
        }
        return null;
    }
}
