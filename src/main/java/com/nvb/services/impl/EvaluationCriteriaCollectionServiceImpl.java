/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.dto.EvaluationCriteriaCollectionListDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.EvaluationCriteriaCollectionDetail;
import com.nvb.pojo.EvaluationCriteriaCollectionDetailPK;
import com.nvb.repositories.EvaluationCriteriaCollectionRepository;
import com.nvb.repositories.EvaluationCriteriaRepository;
import com.nvb.services.EvaluationCriteriaCollectionService;
import com.nvb.repositories.UserRepository;
import com.nvb.repositories.AcademicsStaffRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvb.services.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class EvaluationCriteriaCollectionServiceImpl implements EvaluationCriteriaCollectionService {

    @Autowired
    private EvaluationCriteriaCollectionRepository evaluationCriteriaCollectionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicsStaffRepository academicsStaffRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Override
    public List<EvaluationCriteriaCollectionDTO> getAll(Map<String, String> params) {
        return this.getAll(params, false, false);
    }

    @Override
    public List<EvaluationCriteriaCollectionDTO> getAll(Map<String, String> params, boolean pagination) {
        return this.getAll(params, pagination, false);
    }

    @Override
    public List<EvaluationCriteriaCollectionDTO> getAll(Map<String, String> params, boolean pagination, boolean details) {
        return this.evaluationCriteriaCollectionRepository.getAll(params, pagination, details)
                .stream().map(this::toEvaluationCriteriaCollectionDTO).collect(Collectors.toList());
    }

    @Override
    public List<EvaluationCriteriaCollectionListDTO> getAllForListView(Map<String, String> params, boolean pagination) {
        return this.evaluationCriteriaCollectionRepository.getAll(params, pagination, false) // details = false
                .stream().map(this::toEvaluationCriteriaCollectionListDTO).collect(Collectors.toList());
    }

    @Override
    public EvaluationCriteriaCollectionDTO addOrUpdate(EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffRepository.get(Map.of("username", authentication.getName()));
        }

        // Chuẩn bị map EvaluationCriteria
        Set<Integer> criteriaIds = Optional.ofNullable(evaluationCriteriaCollectionDTO.getEvaluationCriterias())
                .map(criterias -> criterias.stream()
                .filter(c -> c.isSelectedForCollection() && c.getId() != null)
                .map(EvaluationCriteriaDTO::getId)
                .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, EvaluationCriteria> criteriaMap = criteriaIds.isEmpty() ? new HashMap<>()
                : evaluationCriteriaRepository.getByIds(new ArrayList<>(criteriaIds)).stream()
                        .collect(Collectors.toMap(EvaluationCriteria::getId, c -> c));

        EvaluationCriteriaCollection evaluationCriteriaCollection;

        if (evaluationCriteriaCollectionDTO.getId() == null) {
            evaluationCriteriaCollection = new EvaluationCriteriaCollection();
            evaluationCriteriaCollection.setEvaluationCriteriaCollectionDetails(new HashSet<>());
        } else {
            evaluationCriteriaCollection = evaluationCriteriaCollectionRepository.get(
                    Map.of("id", String.valueOf(evaluationCriteriaCollectionDTO.getId())));

            evaluationCriteriaCollection.getEvaluationCriteriaCollectionDetails().clear();
        }

        evaluationCriteriaCollection.setName(evaluationCriteriaCollectionDTO.getName());
        evaluationCriteriaCollection.setDescription(evaluationCriteriaCollectionDTO.getDescription());
        evaluationCriteriaCollection.setCreatedBy(academicStaff);

        evaluationCriteriaCollection = evaluationCriteriaCollectionRepository.addOrUpdate(evaluationCriteriaCollection);

        // Thêm các chi tiết mới từ map đã chuẩn bị
        if (!criteriaIds.isEmpty()) {
            for (EvaluationCriteriaDTO criteriaDTO : evaluationCriteriaCollectionDTO.getEvaluationCriterias()) {
                if (criteriaDTO.isSelectedForCollection() && criteriaDTO.getId() != null) {
                    EvaluationCriteria criteria = criteriaMap.get(criteriaDTO.getId());
                    if (criteria == null) {
                        System.err.println("Cảnh báo: Không tìm thấy tiêu chí với ID " + criteriaDTO.getId());
                        continue;
                    }

                    EvaluationCriteriaCollectionDetailPK pk = new EvaluationCriteriaCollectionDetailPK(
                            evaluationCriteriaCollection.getId(),
                            criteria.getId());
                    EvaluationCriteriaCollectionDetail detail = new EvaluationCriteriaCollectionDetail(pk);
                    detail.setEvaluationCriteriaCollection(evaluationCriteriaCollection);
                    detail.setEvaluationCriteria(criteria);
                    detail.setWeight(criteriaDTO.getWeight() != null ? criteriaDTO.getWeight() : 0f);
                    evaluationCriteriaCollection.getEvaluationCriteriaCollectionDetails().add(detail);
                }
            }
        }

        // Lưu lại với các chi tiết mới
        EvaluationCriteriaCollection savedCollection = evaluationCriteriaCollectionRepository.addOrUpdate(evaluationCriteriaCollection);
        return toEvaluationCriteriaCollectionDTO(savedCollection);
    }

    private EvaluationCriteriaCollectionDTO toEvaluationCriteriaCollectionDTO(EvaluationCriteriaCollection ecc) {
        EvaluationCriteriaCollectionDTO dto = modelMapper.map(ecc, EvaluationCriteriaCollectionDTO.class);
        if (ecc.getCreatedBy() != null) {
            dto.setCreatedByName(ecc.getCreatedBy().getFirstName() + " " + ecc.getCreatedBy().getLastName());
            dto.setCreatedById(ecc.getCreatedBy().getId());
        }

        if (ecc.getEvaluationCriteriaCollectionDetails() != null) {
            List<EvaluationCriteriaDTO> criteriaDTOs = ecc.getEvaluationCriteriaCollectionDetails().stream()
                    .map(detail -> {
                        EvaluationCriteriaDTO criteriaDto = modelMapper.map(detail.getEvaluationCriteria(), EvaluationCriteriaDTO.class);
                        criteriaDto.setWeight(detail.getWeight());
                        return criteriaDto;
                    })
                    .collect(Collectors.toList());
            dto.setEvaluationCriterias(criteriaDTOs);
        }
        return dto;
    }

    private EvaluationCriteriaCollectionListDTO toEvaluationCriteriaCollectionListDTO(EvaluationCriteriaCollection ecc) {
        EvaluationCriteriaCollectionListDTO listDto = modelMapper.map(ecc, EvaluationCriteriaCollectionListDTO.class);
        if (ecc.getCreatedBy() != null) {
            listDto.setCreatedByName(ecc.getCreatedBy().getFirstName() + " " + ecc.getCreatedBy().getLastName());
        }
        return listDto;
    }

    @Override
    public void delete(int id) {
        evaluationCriteriaCollectionRepository.delete(id);
    }

    @Override
    public EvaluationCriteriaCollectionDTO get(Map<String, String> params) {
        EvaluationCriteriaCollection entity = evaluationCriteriaCollectionRepository.get(params);
        return entity != null ? toEvaluationCriteriaCollectionDTO(entity) : null;
    }

}
