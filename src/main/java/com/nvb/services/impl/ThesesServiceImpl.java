/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.EvaluationCriteriaCollectionDetail;
import com.nvb.pojo.EvaluationCriteriaCollectionDetailPK;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.ThesesRepository;
import com.nvb.services.AcademicsStaffService;
import com.nvb.services.ThesesService;
import com.nvb.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class ThesesServiceImpl implements ThesesService {

    @Autowired
    private ThesesRepository thesesRepository;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private AcademicsStaffService academicsStaffService;

    @Override
    public List<Thesis> getTheses(Map<String, String> params) {
        return thesesRepository.getTheses(params);
    }

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params) {
        return thesesRepository.getEvaluationCriteriaCollections(params);
    }

    @Override
    public List<EvaluationCriteriaDTO> getEvaluationCriterias(Map<String, String> params) {
        return thesesRepository.getEvaluationCriteriasDTO(params);
    }

    @Override
    public EvaluationCriteria addEvaluationCriteria(EvaluationCriteriaDTO evaluationCriteriaDTO) {
        EvaluationCriteria evaluationCriteria = new EvaluationCriteria();
        evaluationCriteria.setName(evaluationCriteriaDTO.getName());
        evaluationCriteria.setDescription(evaluationCriteriaDTO.getDescription());
        evaluationCriteria.setMaxPoint(evaluationCriteriaDTO.getMaxPoint());
        return thesesRepository.addEvaluationCriteria(evaluationCriteria);
    }

    @Override
    public EvaluationCriteriaCollection addEvaluationCriteriaCollection(EvaluationCriteriaCollectionDTO evaluationCriteriaCollectionDTO) {
        EvaluationCriteriaCollection evaluationCriteriaCollection = new EvaluationCriteriaCollection();
        evaluationCriteriaCollection.setName(evaluationCriteriaCollectionDTO.getName());
        evaluationCriteriaCollection.setDescription(evaluationCriteriaCollectionDTO.getDescription());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            AcademicStaff academicStaff = academicsStaffService.getAcademicStaff(Map.of("username", authentication.getName()));
            evaluationCriteriaCollection.setCreatedBy(academicStaff);
        }

        EvaluationCriteriaCollection savedEvaluationCriteriaCollection = thesesRepository.addOrUpdateEvaluationCriteriaCollection(evaluationCriteriaCollection);

        Set<EvaluationCriteriaCollectionDetail> details = new HashSet<>();
        if (evaluationCriteriaCollectionDTO.getSelectedCriteriaIds() != null && !evaluationCriteriaCollectionDTO.getSelectedCriteriaIds().isEmpty()) {
            for (EvaluationCriteriaDTO criteria : evaluationCriteriaCollectionDTO.getEvaluationCriterias()) {

                if (evaluationCriteriaCollectionDTO.getSelectedCriteriaIds().contains(criteria.getId())) {
                   
                    EvaluationCriteria evaluationCriteriaEntity = thesesRepository.findEvaluationCriteriaEntityById(criteria.getId());
                    if (evaluationCriteriaEntity == null) {
                        // Xử lý trường hợp không tìm thấy entity, có thể throw exception
                        System.err.println("EvaluationCriteria entity not found for ID: " + criteria.getId());
                        continue; // Hoặc throw new RuntimeException("EvaluationCriteria not found with id: " + criteriaDTO.getId());
                    }

                    EvaluationCriteriaCollectionDetailPK pk = new EvaluationCriteriaCollectionDetailPK(savedEvaluationCriteriaCollection.getId(), evaluationCriteriaEntity.getId());
                    EvaluationCriteriaCollectionDetail detail = new EvaluationCriteriaCollectionDetail(pk);

                    detail.setWeight(criteria.getWeight() != null ? criteria.getWeight() : 0f); // Đảm bảo weight không null nếu DB yêu cầu
                    detail.setEvaluationCriteriaCollection(savedEvaluationCriteriaCollection);
                    detail.setEvaluationCriteria(evaluationCriteriaEntity);

                    details.add(detail);
                }

            }
        }
        if (!details.isEmpty()) {
            // ====> DÒNG QUAN TRỌNG BỊ THIẾU <====
            savedEvaluationCriteriaCollection.setEvaluationCriteriaCollectionDetails(details);
        } else {
            // Nếu không có details nào, đảm bảo collection details là một set rỗng hoặc null tùy theo logic
            savedEvaluationCriteriaCollection.setEvaluationCriteriaCollectionDetails(new HashSet<>());
        }
        return thesesRepository.addOrUpdateEvaluationCriteriaCollection(savedEvaluationCriteriaCollection);
    }

}
