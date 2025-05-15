/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.pojo.EvaluationCriteria;
import com.nvb.pojo.EvaluationCriteriaCollection;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.ThesesRepository;
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
@Transactional
@Repository
public class ThesesRepositoryImpl implements ThesesRepository {

    private static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Thesis> getTheses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Thesis> query = builder.createQuery(Thesis.class);
        Root<Thesis> root = query.from(Thesis.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(builder.equal(root.get("title"), title));
            }
            
            String page = params.get("page");
            if (page == null) {
                params.put("page", "1");
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
    public List<EvaluationCriteriaCollection> getEvaluationCriteriaCollections(Map<String, String> params) {
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
            
            String page = params.get("page");
            if(page == null){
                params.put("page", "1");
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
    public List<EvaluationCriteriaDTO> getEvaluationCriteriasDTO(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<EvaluationCriteriaDTO> query = builder.createQuery(EvaluationCriteriaDTO.class);
        Root<EvaluationCriteria> root = query.from(EvaluationCriteria.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(builder.equal(root.get("name"), name));
            }
            
            String page = params.get("page");
            if(page == null){
                params.put("page", "1");
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.select(builder.construct(
                EvaluationCriteriaDTO.class,
                root.get("id"),
                root.get("name"),
                root.get("description"),
                root.get("maxPoint")
        ));
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
    public EvaluationCriteria addEvaluationCriteria(EvaluationCriteria evaluationCriteria) {
        Session s = factory.getObject().getCurrentSession();
        s.persist(evaluationCriteria);
        return evaluationCriteria;
    }

    @Override
    public EvaluationCriteriaCollection addOrUpdateEvaluationCriteriaCollection(EvaluationCriteriaCollection evaluationCriteriaCollection) {
        Session s = factory.getObject().getCurrentSession();
        if(evaluationCriteriaCollection.getId() != null){
            s.merge(evaluationCriteriaCollection);  
        }else{
            
            s.persist(evaluationCriteriaCollection);
        }
        return evaluationCriteriaCollection;
    }

    @Override
    public List<EvaluationCriteria> getEvaluationCriterias(Map<String, String> params) {
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
        return q.getResultList();}

    @Override
    public EvaluationCriteria findEvaluationCriteriaEntityById(Integer id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(EvaluationCriteria.class, id);
    }
}

