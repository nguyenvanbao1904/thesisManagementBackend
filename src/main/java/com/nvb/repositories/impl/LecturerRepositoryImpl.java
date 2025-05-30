/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.Lecturer;
import com.nvb.repositories.LecturerRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
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
public class LecturerRepositoryImpl implements LecturerRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Lecturer get(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Lecturer> query = builder.createQuery(Lecturer.class);
        Root<Lecturer> root = query.from(Lecturer.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), Integer.parseInt(id)));
            }

            String username = params.get("username");
            if (username != null && !username.isEmpty()) {
                predicates.add(builder.equal(root.get("username"), username));
            }
        }

        // Không cần fetch user vì đã sử dụng inheritance
        // Chỉ cần fetch các relationship khác nếu cần
        if (params != null) {
            if (Boolean.parseBoolean(params.getOrDefault("fetchThesesSupervisors", "false"))) {
                root.fetch("thesesSupervisors", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchCommitteeMembers", "false"))) {
                root.fetch("committeeMembers", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchEvaluationScores", "false"))) {
                root.fetch("evaluationScores", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchThesesReviewer", "false"))) {
                root.fetch("thesesReviewer", JoinType.LEFT);
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.select(root);

        try {
            return s.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Lecturer> getAll(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Lecturer> query = builder.createQuery(Lecturer.class);
        Root<Lecturer> root = query.from(Lecturer.class);

        // Không cần fetch user vì đã sử dụng inheritance
        // Chỉ cần fetch các relationship khác nếu cần
        if (params != null) {
            if (Boolean.parseBoolean(params.getOrDefault("fetchThesesSupervisors", "false"))) {
                root.fetch("thesesSupervisors", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchCommitteeMembers", "false"))) {
                root.fetch("committeeMembers", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchEvaluationScores", "false"))) {
                root.fetch("evaluationScores", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchThesesReviewer", "false"))) {
                root.fetch("thesesReviewer", JoinType.LEFT);
            }
        }

        query.select(root);
        return s.createQuery(query).getResultList();
    }

    @Override
    public List<Lecturer> getByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Lecturer> query = builder.createQuery(Lecturer.class);
        Root<Lecturer> root = query.from(Lecturer.class);
        query.select(root);
        query.where(root.get("id").in(ids));
        return s.createQuery(query).getResultList();
    }
}
