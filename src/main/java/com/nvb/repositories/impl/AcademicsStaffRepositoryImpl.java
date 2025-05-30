/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.AcademicStaff;
import com.nvb.repositories.AcademicsStaffRepository;
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
public class AcademicsStaffRepositoryImpl implements AcademicsStaffRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<AcademicStaff> getAll(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<AcademicStaff> query = builder.createQuery(AcademicStaff.class);
        Root<AcademicStaff> root = query.from(AcademicStaff.class);

        if (params != null) {
            if (Boolean.parseBoolean(params.getOrDefault("fetchCommittees", "false"))) {
                root.fetch("committees", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchEvaluationCriteriaCollections", "false"))) {
                root.fetch("evaluationCriteriaCollections", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchTheses", "false"))) {
                root.fetch("theses", JoinType.LEFT);
            }
        }

        query.select(root);
        return s.createQuery(query).getResultList();
    }

    @Override
    public AcademicStaff get(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<AcademicStaff> query = builder.createQuery(AcademicStaff.class);
        Root<AcademicStaff> root = query.from(AcademicStaff.class);

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

        if (params != null) {
            if (Boolean.parseBoolean(params.getOrDefault("fetchCommittees", "false"))) {
                root.fetch("committees", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchEvaluationCriteriaCollections", "false"))) {
                root.fetch("evaluationCriteriaCollections", JoinType.LEFT);
            }
            if (Boolean.parseBoolean(params.getOrDefault("fetchTheses", "false"))) {
                root.fetch("theses", JoinType.LEFT);
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

}
