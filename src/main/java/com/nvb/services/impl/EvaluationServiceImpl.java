/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.EvaluationFinalScoreDTO;
import com.nvb.dto.EvaluationScoreDTO;
import com.nvb.pojo.CommitteeStatus;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.EvaluationScore;
import com.nvb.pojo.EvaluationScorePK;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Thesis;
import com.nvb.pojo.ThesisStatus;
import com.nvb.repositories.EvaluationCriteriaRepository;
import com.nvb.repositories.EvaluationRepository;
import com.nvb.repositories.ThesesRepository;
import com.nvb.repositories.UserRepository;
import com.nvb.services.EvaluationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Transactional
@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationCriteriaRepository evaluationCriteriaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThesesRepository thesesRepository;

    @Override
    public void evaluate(List<EvaluationScoreDTO> evaluationScoreDTOs) {

        // Chuẩn bị map EvaluationCriteria
        Set<Integer> criteriaIds = Optional.ofNullable(evaluationScoreDTOs)
                .map(es -> es.stream()
                .map(e -> e.getCriteriaId())
                .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, EvaluationCriteria> criteriaMap = criteriaIds.isEmpty() ? new HashMap<>()
                : evaluationCriteriaRepository.getByIds(new ArrayList<>(criteriaIds)).stream()
                        .collect(Collectors.toMap(EvaluationCriteria::getId, c -> c));

        // Chuẩn bị map theses
        Set<Integer> thesesIds = Optional.ofNullable(evaluationScoreDTOs)
                .map(es -> es.stream()
                .map(e -> e.getThesisId())
                .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, Thesis> thesesMap = thesesIds.isEmpty() ? new HashMap<>()
                : thesesRepository.getByIds(new ArrayList<>(thesesIds)).stream()
                        .collect(Collectors.toMap(Thesis::getId, t -> t));

        // Chuẩn bị map lecturer
        Set<Integer> lecturerIds = Optional.ofNullable(evaluationScoreDTOs)
                .map(es -> es.stream()
                .map(e -> e.getLecturerId())
                .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, Lecturer> lecturerMap = lecturerIds.isEmpty() ? new HashMap<>()
                : userRepository.getByIds(new ArrayList<>(lecturerIds)).stream()
                        .map(u -> (Lecturer) u)
                        .collect(Collectors.toMap(Lecturer::getId, l -> l));

        evaluationScoreDTOs.forEach(evaluationScoreDTO -> {

            Thesis thesis = thesesMap.get(evaluationScoreDTO.getThesisId());

            if (thesis == null) {
                throw new RuntimeException("Không tìm thấy luận văn");
            }

            if (thesis.getCommitteeId().getStatus().equals(CommitteeStatus.LOCKED.toString())) {
                throw new RuntimeException("Hội đồng của luận văn \"" + thesis.getTitle() + "\" đã bị khóa.");
            }

            if (thesis.getStatus().equals(ThesisStatus.COMPLETED.toString())) {
                throw new RuntimeException("Luận văn \"" + thesis.getTitle() + "\" đã hoàn thành, không thể chỉnh sửa điểm.");
            }

            EvaluationScore evaluationScore = new EvaluationScore();
            EvaluationScorePK pk
                    = new EvaluationScorePK(evaluationScoreDTO.getThesisId(), evaluationScoreDTO.getCriteriaId(), evaluationScoreDTO.getLecturerId());
            evaluationScore.setEvaluationScorePK(pk);
            evaluationScore.setComment(evaluationScoreDTO.getComment());
            evaluationScore.setEvaluationCriteria(criteriaMap.get(evaluationScoreDTO.getCriteriaId()));
            evaluationScore.setLecturer(lecturerMap.get(evaluationScoreDTO.getLecturerId()));
            evaluationScore.setScore(evaluationScoreDTO.getScore());
            evaluationScore.setThesis(thesesMap.get(evaluationScoreDTO.getThesisId()));
            evaluationRepository.addOrUpdateEvaluation(evaluationScore);
        });
    }

    @Override
    public List<EvaluationScoreDTO> getEvaluation(Map<String, String> params) {
        List<EvaluationScore> evaluationScores = evaluationRepository.getEvaluation(params);
        return evaluationScores.stream().map(e -> {
            EvaluationScoreDTO evaluationScoreDTO = new EvaluationScoreDTO();
            evaluationScoreDTO.setComment(e.getComment());
            evaluationScoreDTO.setCriteriaId(e.getEvaluationCriteria().getId());
            evaluationScoreDTO.setCriteriaName(e.getEvaluationCriteria().getName());
            evaluationScoreDTO.setLecturerId(e.getLecturer().getId());
            evaluationScoreDTO.setLecturerName(e.getLecturer().getFirstName() + " " + e.getLecturer().getLastName());
            evaluationScoreDTO.setScore(e.getScore());
            evaluationScoreDTO.setThesisId(e.getThesis().getId());
            evaluationScoreDTO.setThesisTitle(e.getThesis().getTitle());
            return evaluationScoreDTO;
        }).toList();
    }

    @Override
    public EvaluationFinalScoreDTO getFinalScore(Integer thesisId) {
        EvaluationFinalScore score = evaluationRepository.getFinalScore(thesisId);
        if(score == null){
            return null;
        }
        EvaluationFinalScoreDTO rs = new EvaluationFinalScoreDTO();
        rs.setAverageScore(Float.valueOf(String.valueOf(score.getAverageScore())));
        rs.setChairmanComment(score.getChairmanComment());
        rs.setId(score.getId());
        rs.setThesisId(score.getThesisId());
        rs.setThesisTitle(score.getThesis().getTitle());
        return rs;
    }

}
