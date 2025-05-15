/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.AcademicStaff;
import com.nvb.repositories.AcademicsStaffRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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
public class AcademicsStaffRepositoryImpl implements AcademicsStaffRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public AcademicStaff getAcademicStaff(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<AcademicStaff> query = builder.createQuery(AcademicStaff.class);
        Root<AcademicStaff> root = query.from(AcademicStaff.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), id));
            }
        }
        
        String username = params.get("username");
        if (username != null && !username.isEmpty()) {
            Join<Object, Object> userJoin = root.join("user");
            predicates.add(builder.equal(userJoin.get("username"), username));
        }

        query.where(predicates.toArray(new Predicate[0]));

        Query q = s.createQuery(query);
        try {
            return (AcademicStaff) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (org.hibernate.NonUniqueResultException ex) {
            System.err.println(ex.getMessage());
            return null; 
        }
    }

}
