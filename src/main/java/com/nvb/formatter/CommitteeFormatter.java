package com.nvb.formatter;

import com.nvb.pojo.Committee;
import com.nvb.repositories.CommitteeRepository;
// import com.nvb.services.CommitteeService; // You will need to create and import this
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CommitteeFormatter implements Formatter<Committee> {

    @Autowired
    private CommitteeRepository committeeRepository;

    // @Autowired
    // private CommitteeService committeeService; // You will need to create and autowire this service

    @Override
    public String print(Committee object, Locale locale) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object.getId());
    }

    @Override
    public Committee parse(String id, Locale locale) throws ParseException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        try {
            int committeeId = Integer.parseInt(id);
            Committee committee = committeeRepository.get(Map.of("id", String.valueOf(committeeId))); 
            if (committee == null) {
                 return null; 
            }
            return committee;
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid ID format for Committee: " + id, 0);
        } catch (Exception e) { 
            throw new ParseException("Cannot parse " + id + " to Committee. Error: " + e.getMessage(), 0);
        }
    }
} 