package com.nvb.formatter;

import com.nvb.pojo.Lecturer;
import com.nvb.services.LecturerService;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class LecturerFormatter implements Formatter<Lecturer> {

    @Autowired
    private LecturerService lecturerService;

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
        Lecturer lecturer = lecturerService.getLecturerWithDetails(Map.of("id", id));
        return lecturer;
    }
}
