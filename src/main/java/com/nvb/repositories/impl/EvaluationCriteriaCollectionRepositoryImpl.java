/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.repositories.EvaluationCriteriaCollectionRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
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
@Transactional
@Repository
public class EvaluationCriteriaCollectionRepositoryImpl implements EvaluationCriteriaCollectionRepository {

    private static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params, boolean pagination) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteriaCollection> query = builder.createQuery(EvaluationCriteriaCollection.class);
        Root<EvaluationCriteriaCollection> root = query.from(EvaluationCriteriaCollection.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", name)));
            }

            if (pagination) {
                String page = params.get("page");
                if (page == null) {
                    params.put("page", "1");
                }
            }
        }

        query.select(root);
        query.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(query);

        if (params != null && params.containsKey("page")) {
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
    public EvaluationCriteriaCollection addOrUpdateEvaluationCriteriaCollection(EvaluationCriteriaCollection evaluationCreiteriaCollection) {
        Session s = factory.getObject().getCurrentSession();
        if (evaluationCreiteriaCollection.getId() == null) {
            s.persist(evaluationCreiteriaCollection);
        } else {
            s.merge(evaluationCreiteriaCollection);
        }
        return evaluationCreiteriaCollection;
    }

    @Override
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollectionsWithDetails(Map<String, String> params, boolean pagination) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteriaCollection> query = builder.createQuery(EvaluationCriteriaCollection.class);
        Root<EvaluationCriteriaCollection> root = query.from(EvaluationCriteriaCollection.class);

        // JOIN FETCH both details and criteria to avoid N+1 queries
        root.fetch("evaluationCriteriaCollectionDetails", JoinType.LEFT)
            .fetch("evaluationCriteria", JoinType.LEFT);

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

            if (pagination) {
                String page = params.get("page");
                if (page == null) {
                    params.put("page", "1");
                }
            }
        }

        query.select(root).distinct(true);
        query.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(query);

        if (params != null && params.containsKey("page")) {
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
    public void deleteEvaluationCriteriaCollection(int id) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(s.get(EvaluationCriteriaCollection.class, id));
    }

    @Override
    public EvaluationCriteriaCollection getEvaluationCriteriaCollection(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteriaCollection> query = builder.createQuery(EvaluationCriteriaCollection.class);
        Root<EvaluationCriteriaCollection> root = query.from(EvaluationCriteriaCollection.class);

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

        query.select(root);
        query.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(query);
        
        try{
            return (EvaluationCriteriaCollection)q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
}
