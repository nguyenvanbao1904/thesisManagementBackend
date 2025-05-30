/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.controllers;

import com.nvb.dto.CommitteeDTO;
import com.nvb.dto.CommitteeListDTO;
import com.nvb.pojo.CommitteeCampus;
import com.nvb.services.CommitteeService;
import com.nvb.validators.WebAppValidator;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
@RequestMapping("/api/committees")
@CrossOrigin
public class ApiCommitteeControllers {

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    @Qualifier("committeeWebAppValidator")
    private WebAppValidator committeeWebAppValidator;

    @InitBinder()
    public void initCommitteeWebAppValidatorBinder(WebDataBinder binder) {
        binder.setValidator(committeeWebAppValidator);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        committeeService.delete(id);
    }

    @GetMapping("")
    public ResponseEntity<List<CommitteeListDTO>> list(@RequestParam Map<String, String> params) {
        List<CommitteeListDTO> collections = this.committeeService.getAllForListView(params, true);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommitteeDTO> retrieve(@PathVariable(value = "id") int id) {
        CommitteeDTO committeeDTO = this.committeeService.get(new HashMap<>(Map.of("id", String.valueOf(id))));
        return new ResponseEntity<>(committeeDTO, HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<CommitteeCampus>> listLocation(@RequestParam Map<String, String> params) {
        List<CommitteeCampus> rs = List.of(CommitteeCampus.values());
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CommitteeDTO> create(Model model, @Valid @ModelAttribute("committee") CommitteeDTO committeeDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CommitteeDTO rs = committeeService.addOrUpdate(committeeDTO);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
    
    @PutMapping(value = "/{id}")
    public ResponseEntity<CommitteeDTO> update(
            @PathVariable(value = "id") int id,
            @ModelAttribute @Valid CommitteeDTO committeeDTO,
            BindingResult bindingResult
    ) {
        if (committeeDTO.getId() != id || bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CommitteeDTO rs = committeeService.addOrUpdate(committeeDTO);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
