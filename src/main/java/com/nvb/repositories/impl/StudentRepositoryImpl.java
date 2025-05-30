/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.Student;
import com.nvb.pojo.User;
import com.nvb.repositories.StudentRepository;
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
@Repository
@Transactional
public class StudentRepositoryImpl implements StudentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Student get(Map<String, String> params, boolean details) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Student> query = builder.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);
        query.select(root);

        if (details) {
            root.fetch("theses", JoinType.LEFT);
        }

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String studentId = params.get("studentId");
            if (studentId != null && !studentId.isEmpty()) {
                predicates.add(builder.equal(root.get("studentId"), studentId));
            }
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), id));
            }

        }

        query.where(predicates.toArray(new Predicate[0]));

        Query q = s.createQuery(query);
        try {
            return (Student) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (org.hibernate.NonUniqueResultException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Student> getAll(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Student> query = builder.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String studentId = params.get("studentId");
            if (studentId != null && !studentId.isEmpty()) {
                predicates.add(builder.equal(root.get("studentId"), studentId));
            }
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), id));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        Query q = s.createQuery(query);
        try {
            return q.getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }

    @Override
    public Student addOrUpdate(Student student) {
        Session s = this.factory.getObject().getCurrentSession();
        if (student.getId() == null) {
            s.persist(student);
        } else {
            s.merge(student);
        }
        return student;
    }
}
