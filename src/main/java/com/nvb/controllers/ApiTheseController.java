/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.ThesesDTO;
import com.nvb.dto.ThesesListDTO;
import com.nvb.dto.UserDTO;
import com.nvb.services.EmailService;
import com.nvb.services.ThesesService;
import com.nvb.services.UserService;
import com.nvb.validators.WebAppValidator;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyenvanbao
 */
@RestController
@RequestMapping("/api/theses")
@CrossOrigin
public class ApiTheseController {

    @Autowired
    private ThesesService thesesService;
    
    @Autowired
    private UserService userDetailsService;

    @Autowired
    @Qualifier("thesesWebAppValidator")
    private WebAppValidator thesesWebAppValidator;

    @Autowired
    private EmailService emailService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(thesesWebAppValidator);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        thesesService.delete(id);
    }

    @GetMapping("")
    public ResponseEntity<List<ThesesListDTO>> list(@RequestParam Map<String, String> params) {
        List<ThesesListDTO> collections = thesesService.getAllForListView(params, true);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThesesDTO> retrieve(@PathVariable(value = "id") int id) {
        ThesesDTO thesesDTO = thesesService.get(new HashMap<>(Map.of("id", String.valueOf(id))));
        return new ResponseEntity<>(thesesDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ThesesDTO> update(
            @PathVariable(value = "id") int id,
            @ModelAttribute @Valid ThesesDTO thesesDTO,
            BindingResult bindingResult
    ) {
        if (thesesDTO.getId() != id || bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ThesesDTO rs = thesesService.addOrUpdate(thesesDTO);
         if (thesesDTO.getReviewerId() != null) {
            try {
                String subject = "Thông báo phản biện khóa luận";
                String body = String.format(
                        "<h3>Thân gửi giảng viên,</h3>"
                        + "<p>Bạn vừa được phân công phản biện khóa luận <strong>%s</strong>.</p>",
                        thesesDTO.getTitle()
                );
                UserDTO reviewer = userDetailsService.get(Map.of("id", String.valueOf(thesesDTO.getReviewerId())));
                if (reviewer != null) {
                    emailService.sendEmail(reviewer.getEmail(), subject, body);
                }
            } catch (MessagingException e) {
            }
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ThesesDTO> create(@ModelAttribute @Valid ThesesDTO thesesDTO, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        ThesesDTO rs = thesesService.addOrUpdate(thesesDTO);
         if (thesesDTO.getReviewerId() != null) {
            try {
                String subject = "Thông báo phản biện khóa luận";
                String body = String.format(
                        "<h3>Thân gửi giảng viên,</h3>"
                        + "<p>Bạn vừa được phân công phản biện khóa luận <strong>%s</strong>.</p>",
                        thesesDTO.getTitle()
                );
                UserDTO reviewer = userDetailsService.get(Map.of("id", String.valueOf(thesesDTO.getReviewerId())));
                if (reviewer != null) {
                    emailService.sendEmail(reviewer.getEmail(), subject, body);
                }
            } catch (MessagingException e) {
            }
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
