/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Committee;
import com.nvb.repositories.CommitteeRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
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
public class CommitteeRepositoryImpl implements CommitteeRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Committee> getAll(Map<String, String> params, boolean pagination, boolean details) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();

        if (!details) {
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<Committee> root = query.from(Committee.class);
            Join<Committee, AcademicStaff> createdByJoin = root.join("createdBy", JoinType.LEFT);
            query.multiselect(
                    root.get("id"),
                    root.get("defenseDate"),
                    root.get("location"),
                    root.get("status"),
                    root.get("isActive"),
                    createdByJoin.get("firstName"),
                    createdByJoin.get("lastName")
            );

            List<Predicate> predicates = new ArrayList<>();
            if (params != null) {
                String status = params.get("status");
                if (status != null && !status.isEmpty()) {
                    predicates.add(builder.equal(root.get("status"), status));
                }
                String location = params.get("location");
                if (location != null && !location.isEmpty()) {
                    predicates.add(builder.like(root.get("location"), String.format("%%%s%%", location)));
                }
            }

            query.where(predicates.toArray(new Predicate[0]));
            Query q = s.createQuery(query);

            if (pagination && params != null && params.containsKey("page")) {
                if (params.get("page") != null && Integer.parseInt(params.get("page")) == 0) {
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

            List<Object[]> results = q.getResultList();
            List<Committee> committees = new ArrayList<>();
            for (Object[] row : results) {
                Committee committee = new Committee();
                committee.setId((Integer) row[0]);
                committee.setDefenseDate((LocalDateTime) row[1]);
                committee.setLocation((String) row[2]);
                committee.setStatus((String) row[3]);
                committee.setIsActive((Boolean) row[4]);

                // Construct createdBy User and AcademicStaff if names are present
                String firstName = (String) row[5];
                String lastName = (String) row[6];
                if (firstName != null || lastName != null) {
                    AcademicStaff createdByStaff = new AcademicStaff();
                    createdByStaff.setFirstName(firstName);
                    createdByStaff.setLastName(lastName);
                    committee.setCreatedBy(createdByStaff);
                }
                committees.add(committee);
            }
            return committees;

        } else {
            CriteriaQuery<Committee> query = builder.createQuery(Committee.class);
            Root<Committee> root = query.from(Committee.class);
            root.fetch("createdBy", JoinType.LEFT).fetch("user", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            if (params != null) {
                String status = params.get("status");
                if (status != null && !status.isEmpty()) {
                    predicates.add(builder.equal(root.get("status"), status));
                }
                String location = params.get("location");
                if (location != null && !location.isEmpty()) {
                    predicates.add(builder.like(root.get("location"), String.format("%%%s%%", location)));
                }
            }

            query.select(root).distinct(true);
            query.where(predicates.toArray(new Predicate[0]));
            Query q = s.createQuery(query);

            if (pagination && params != null && params.containsKey("page")) {
                if (params.get("page") != null && Integer.parseInt(params.get("page")) == 0) {
                    return new ArrayList<>();
                }
                int page = 1;
                try {
                    page = Integer.parseInt(params.get("page"));
                } catch (NumberFormatException ex) {
                    page = 1; // Default to page 1 if parsing fails
                }
                int start = (page - 1) * PAGE_SIZE;
                q.setMaxResults(PAGE_SIZE);
                q.setFirstResult(start);
            }

            return q.getResultList();
        }
    }

    @Override
    public Committee get(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Committee> query = builder.createQuery(Committee.class);
        Root<Committee> root = query.from(Committee.class);

        // Fetch các mối quan hệ
        root.fetch("committeeMembers", JoinType.LEFT);
        root.fetch("theses", JoinType.LEFT);
        root.fetch("createdBy", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), id));
            }
        }

        query.select(root);
        query.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(query);

        try {
            return (Committee) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Committee addOrUpdate(Committee committee) {
        Session s = factory.getObject().getCurrentSession();
        if (committee.getId() == null) {
            s.persist(committee);
        } else {
            s.merge(committee);
        }
        return committee;
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(s.get(Committee.class, id));
    }
}
