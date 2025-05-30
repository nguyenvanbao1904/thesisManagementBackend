/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.EvaluationCriteria;
import com.nvb.repositories.EvaluationCriteriaRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
public class EvaluationCriteriaRepositoryImpl implements EvaluationCriteriaRepository {

    private static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<EvaluationCriteria> getAll(Map<String, String> params, boolean pagination) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteria> query = builder.createQuery(EvaluationCriteria.class);
        Root<EvaluationCriteria> root = query.from(EvaluationCriteria.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", name)));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.select(root);
        Query q = s.createQuery(query);

        if (pagination && params != null && params.containsKey("page")) {
            if (Integer.parseInt(params.get("page")) == 0) {
                return new ArrayList<>();
            }
            int page = 1;
            try {
                page = Integer.parseInt(params.get("page"));
            } catch (NumberFormatException ex) {
                page = 1;
            }
            int start = (page - 1) * PAGE_SIZE;
            q.setMaxResults(PAGE_SIZE);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public EvaluationCriteria get(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteria> query = builder.createQuery(EvaluationCriteria.class);
        Root<EvaluationCriteria> root = query.from(EvaluationCriteria.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(builder.equal(root.get("name"), name));
            }
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), id));
            }

        }

        query.where(predicates.toArray(new Predicate[0]));
        query.select(root);
        Query q = s.createQuery(query);
        try {
            return (EvaluationCriteria) q.getSingleResult();

        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public EvaluationCriteria addOrUpdate(EvaluationCriteria evaluationCriteria) {
        Session s = factory.getObject().getCurrentSession();
        if (evaluationCriteria.getId() == null) {
            s.persist(evaluationCriteria);
        } else {
            s.merge(evaluationCriteria);
        }
        return evaluationCriteria;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(s.get(EvaluationCriteria.class, id));
    }

    @Override
    public List<EvaluationCriteria> getByIds(List<Integer> ids) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteria> query = builder.createQuery(EvaluationCriteria.class);
        Root<EvaluationCriteria> root = query.from(EvaluationCriteria.class);

        query.select(root).where(root.get("id").in(ids));
        return s.createQuery(query).getResultList();
    }
}
