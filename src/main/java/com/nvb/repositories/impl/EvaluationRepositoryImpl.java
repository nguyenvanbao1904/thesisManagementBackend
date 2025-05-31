/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.EvaluationCriteriaCollectionDetail;
import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.EvaluationScore;
import com.nvb.pojo.EvaluationScorePK;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.EvaluationRepository;
import com.nvb.repositories.ThesesRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Repository
@Transactional
public class EvaluationRepositoryImpl implements EvaluationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private ThesesRepository thesesRepository;

    @Override
    public void addOrUpdateEvaluation(EvaluationScore evaluationScore) {
        Session s = factory.getObject().getCurrentSession();
        EvaluationScorePK pk = evaluationScore.getEvaluationScorePK();

        EvaluationScore existing = s.get(EvaluationScore.class, pk);

        if (existing == null) {
            s.persist(evaluationScore);
        } else {
            s.merge(evaluationScore);
        }
    }

    @Override
    public List<EvaluationScore> getEvaluation(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationScore> query = builder.createQuery(EvaluationScore.class);
        Root<EvaluationScore> root = query.from(EvaluationScore.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String thesisId = params.get("thesisId");
            String lecturerId = params.get("lecturerId");

            if (thesisId != null && !thesisId.isEmpty() && lecturerId != null && !lecturerId.isEmpty()) {
                predicates.add(builder.equal(root.get("evaluationScorePK").get("thesisId"), Integer.parseInt(thesisId)));
                predicates.add(builder.equal(root.get("evaluationScorePK").get("lecturerId"), Integer.parseInt(lecturerId)));
            } else if (thesisId != null && !thesisId.isEmpty() && lecturerId == null) {
                predicates.add(builder.equal(root.get("evaluationScorePK").get("thesisId"), Integer.parseInt(thesisId)));
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.select(root);

        Query q = s.createQuery(query);
        return q.getResultList();
    }

    @Override
    public Float calculateAverageScore(Integer thesisId) {
        Session s = this.factory.getObject().getCurrentSession();

        Thesis thesis = thesesRepository.get(new HashMap<>(Map.of("id", thesisId.toString())));

        if (thesis.getCommitteeId() == null || thesis.getEvaluationCriteriaCollectionId() == null) {
            return null;
        }

        // 2. Lấy trọng số của từng tiêu chí
        Map<Integer, Float> criteriaWeights = new HashMap<>();
        for (EvaluationCriteriaCollectionDetail detail : thesis.getEvaluationCriteriaCollectionId().getEvaluationCriteriaCollectionDetails()) {
            criteriaWeights.put(detail.getEvaluationCriteria().getId(), detail.getWeight());
        }

        List<EvaluationScore> scores = this.getEvaluation(new HashMap<>(Map.of("thesisId", thesisId.toString())));

        if (scores.isEmpty()) {
            return null;
        }

        // 4. Tổ chức điểm theo giảng viên và tiêu chí
        Map<Integer, Map<Integer, Float>> lecturerCriteriaScores = new HashMap<>();

        for (EvaluationScore score : scores) {
            int lecturerId = score.getEvaluationScorePK().getLecturerId();
            int criteriaId = score.getEvaluationScorePK().getCriteriaId();
            float scoreValue = score.getScore();

            // Lấy hoặc tạo map điểm theo tiêu chí cho lecturer này
            Map<Integer, Float> criteriaScores = lecturerCriteriaScores.computeIfAbsent(lecturerId, k -> new HashMap<>());
            criteriaScores.put(criteriaId, scoreValue);
        }

        // 5. Tính điểm có trọng số cho từng giảng viên
        Map<Integer, Float> lecturerFinalScores = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Float>> entry : lecturerCriteriaScores.entrySet()) {
            Integer lecturerId = entry.getKey();
            Map<Integer, Float> criteriaScores = entry.getValue();

            float totalWeightedScore = 0;
            float totalWeight = 0;

            for (Map.Entry<Integer, Float> scoreEntry : criteriaScores.entrySet()) {
                Integer criteriaId = scoreEntry.getKey();
                Float score = scoreEntry.getValue();
                Float weight = criteriaWeights.getOrDefault(criteriaId, 1.0f);

                totalWeightedScore += score * weight;
                totalWeight += weight;
            }

            // Tính điểm trung bình có trọng số cho giảng viên này
            float avgScore = totalWeight > 0 ? totalWeightedScore / totalWeight : 0;
            lecturerFinalScores.put(lecturerId, avgScore);
        }

        // 6. Tính điểm trung bình của tất cả thành viên
        if (lecturerFinalScores.isEmpty()) {
            return null;
        }

        float totalScore = 0;
        for (Float score : lecturerFinalScores.values()) {
            totalScore += score;
        }

        return totalScore / lecturerFinalScores.size();
    }

    @Override
    public void addOrUpdateFinalScore(EvaluationFinalScore finalScore) {
        Session s = this.factory.getObject().getCurrentSession();
        if (finalScore.getId() == null) {
            s.persist(finalScore);
        } else {
            s.merge(finalScore);

        }
    }

    @Override
    public EvaluationFinalScore getFinalScore(Integer thesisId) {
        Session s = this.factory.getObject().getCurrentSession();
        try{
            return s.createNamedQuery("EvaluationFinalScore.findByThesisId", EvaluationFinalScore.class)
                .setParameter("thesisId", thesisId)
                .getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
}
