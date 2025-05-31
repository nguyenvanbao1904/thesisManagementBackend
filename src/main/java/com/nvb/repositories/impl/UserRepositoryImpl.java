/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.User;
import com.nvb.pojo.Student;
import com.nvb.pojo.Admin;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Lecturer;
import com.nvb.repositories.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User get(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String userId = params.get("id");
            String username = params.get("username");
            String phone = params.get("phone");
            String email = params.get("email");

            if (userId != null && !userId.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), Integer.parseInt(userId)));
            }

            if (username != null && !username.isEmpty()) {
                predicates.add(builder.equal(root.get("username"), username));
            }

            if (phone != null && !phone.isEmpty()) {
                predicates.add(builder.equal(root.get("phone"), phone));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(builder.equal(root.get("email"), email));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.select(root);

        Query q = s.createQuery(query);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User addOrUpdate(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u.getId() == null) {
            s.persist(u);
        } else {
            s.merge(u);
        }
        return u;
    }

    @Override
    public User authenticate(String username, String password) {
        User u = this.get(Map.of("username", username));
        if (this.passwordEncoder.matches(password, u.getPassword())) {
            return u;
        }
        return null;
    }

    @Override
    public List<User> getAll(Map<String, String> params, boolean pagination, boolean details) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        if (details) {
        }

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("username");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("username"), String.format("%%%s%%", kw)));
            }

            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                switch (role) {
                    case "ROLE_ADMIN":
                        predicates.add(builder.equal(root.type(), Admin.class));
                        break;
                    case "ROLE_ACADEMICSTAFF":
                        predicates.add(builder.equal(root.type(), AcademicStaff.class));
                        break;
                    case "ROLE_LECTURER":
                        predicates.add(builder.equal(root.type(), Lecturer.class));
                        break;
                    case "ROLE_STUDENT":
                        predicates.add(builder.equal(root.type(), Student.class));
                        break;
                }
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                Boolean isActiveBool = Boolean.valueOf(isActive);
                predicates.add(builder.equal(root.get("isActive"), isActiveBool));
            }

            query.where(predicates.toArray(Predicate[]::new));
        }
        query.select(root).distinct(true);

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
    public void delete(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.remove(u);
    }

    @Override
    public List<User> getByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(root.get("id").in(ids));
        query.distinct(true); 
        return s.createQuery(query).getResultList();
    }
}
