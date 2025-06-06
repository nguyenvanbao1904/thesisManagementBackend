/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.Major;
import com.nvb.repositories.MajorRepository;
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
public class MajorRepositoryImpl implements MajorRepository{

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public Major addOrUpdate(Major major) {
        Session s = factory.getObject().getCurrentSession();
        if (major.getId() == null) {
            s.persist(major);
        } else {
            s.merge(major);
        }
        return major;
    }

    @Override
    public List<Major> getAll(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        
        CriteriaBuilder buider = s.getCriteriaBuilder();
        CriteriaQuery<Major> query = buider.createQuery(Major.class);
        Root root = query.from(Major.class);
        query.select(root);
        
        if (params != null) {
            
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(buider.like(root.get("name"), String.format("%%%s%%", kw)));
            }
            
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                Boolean isActiveBool = Boolean.valueOf(isActive);
                predicates.add(buider.equal(root.get("isActive"), isActiveBool));
            }
            
            String page = params.get("page");
            if(page == null){
                params.put("page", "1");
            }
            query.where(predicates.toArray(Predicate[]::new));
           
        }
        Query q = s.createQuery(query);

        
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            q.setMaxResults(PAGE_SIZE);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public Major get(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        
        CriteriaBuilder buider = s.getCriteriaBuilder();
        CriteriaQuery<Major> query = buider.createQuery(Major.class);
        Root root = query.from(Major.class);
        query.select(root);
        
        if (params != null) {
            
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(buider.equal(root.get("name"), kw));
            }
            
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(buider.equal(root.get("id"), id));
            }
            
            query.where(predicates.toArray(Predicate[]::new));
           
        }
        Query q = s.createQuery(query);
        
        try{
            return (Major) q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }

    @Override
    public void delete(int id){
        Session s = this.factory.getObject().getCurrentSession();
        s.remove(s.get(Major.class, id));
    }
    
}
