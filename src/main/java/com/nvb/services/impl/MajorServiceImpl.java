/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.pojo.Major;
import com.nvb.repositories.MajorRepository;
import com.nvb.services.MajorService;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvb.dto.MajorDTO;
import java.util.HashMap;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class MajorServiceImpl implements MajorService{

    @Autowired
    private MajorRepository majorRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public List<MajorDTO> getAll(Map<String, String> params) {
        List<Major> majors = majorRepository.getAll(params);
        return majors.stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public MajorDTO get(Map<String, String> params) {
        Major major = majorRepository.get(params);
        return major != null ? toDTO(major) : null;
    }

    @Override
    public void delete(int id) {
        majorRepository.delete(id);
    }

    @Override
    public MajorDTO addOrUpdate(MajorDTO majorDTO) {
        Major major;
        if (majorDTO.getId() == null) {
            major = new Major();
        } else {
            major = majorRepository.get(new HashMap<>(Map.of("id", majorDTO.getId().toString())));
            if (major == null) {
                major = new Major();
            }
        }
        major.setName(majorDTO.getName());
        major.setIsActive(majorDTO.getIsActive() != null ? majorDTO.getIsActive() : true);
        Major saved = majorRepository.addOrUpdate(major);
        return toDTO(saved);
    }

    private MajorDTO toDTO(Major major) {
        return modelMapper.map(major, MajorDTO.class);
    }
}
