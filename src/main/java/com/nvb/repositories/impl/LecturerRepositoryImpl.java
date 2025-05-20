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
import jakarta.persistence.criteria.Root;
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
    public Lecturer getLecturerWithDetails(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Lecturer> query = builder.createQuery(Lecturer.class);
        Root<Lecturer> root = query.from(Lecturer.class);

        // Eager fetch theses to ensure the relationship is loaded
        root.fetch("thesesSupervisors", JoinType.LEFT);
        root.fetch("user", JoinType.LEFT);

        query.select(root);
        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                query.where(builder.equal(root.get("id"), id));
            }
        }

        try {
            return s.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
