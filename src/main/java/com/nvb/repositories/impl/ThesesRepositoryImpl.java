/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.dto.UserDTO;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import com.nvb.pojo.Thesis;
import com.nvb.pojo.User;
import com.nvb.repositories.ThesesRepository;
import com.nvb.repositories.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Thesis> getAll(Map<String, String> params, boolean pagination, boolean details) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();

        if (!details) {
            // Use projection query for list view
            CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
            Root<Thesis> root = query.from(Thesis.class);

            query.multiselect(
                    root.get("id"),
                    root.get("title"),
                    root.get("description"),
                    root.get("status"),
                    root.get("fileUrl")
            );

            List<Predicate> predicates = new ArrayList<>();

            if (params != null) {
                String title = params.get("title");
                if (title != null && !title.isEmpty()) {
                    predicates.add(builder.like(root.get("title"), String.format("%%%s%%", title)));
                }
                String committeeId = params.get("committeeId");
                if (committeeId != null && committeeId.isEmpty()) {
                    predicates.add(builder.isNull(root.get("committeeId")));
                }

                if (pagination) {
                    String page = params.get("page");
                    if (page == null || page.isEmpty()) {
                        params.put("page", "1");
                    }
                }
            }

            query.where(predicates.toArray(new Predicate[0]));
            Query q = s.createQuery(query);

            if (params != null && params.containsKey("page")) {
                int page = Integer.parseInt(params.get("page"));
                int start = (page - 1) * PAGE_SIZE;

                q.setMaxResults(PAGE_SIZE);
                q.setFirstResult(start);
            }

            List<Object[]> results = q.getResultList();
            List<Thesis> theses = new ArrayList<>();
            for (Object[] row : results) {
                Thesis thesis = new Thesis();
                thesis.setId((Integer) row[0]);
                thesis.setTitle((String) row[1]);
                thesis.setDescription((String) row[2]);
                thesis.setStatus((String) row[3]);
                thesis.setFileUrl((String) row[4]);
                theses.add(thesis);
            }
            return theses;
        } else {
            CriteriaQuery<Thesis> query = builder.createQuery(Thesis.class);
            Root<Thesis> root = query.from(Thesis.class);

            root.fetch("reviewerId", JoinType.LEFT).fetch("user", JoinType.LEFT);
            root.fetch("evaluationCriteriaCollectionId", JoinType.LEFT);
            root.fetch("committeeId", JoinType.LEFT);
            root.fetch("lecturers", JoinType.LEFT).fetch("user", JoinType.LEFT);
            root.fetch("students", JoinType.LEFT).fetch("user", JoinType.LEFT);
            root.fetch("evaluationFinalScore", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (params != null) {
                String title = params.get("title");
                if (title != null && !title.isEmpty()) {
                    predicates.add(builder.like(root.get("title"), String.format("%%%s%%", title)));
                }
                String committeeId = params.get("committeeId");
                if (committeeId != null && committeeId.isEmpty()) {
                    predicates.add(builder.isNull(root.get("committeeId")));
                }
            }

            query.select(root).distinct(true);
            query.where(predicates.toArray(new Predicate[0]));
            Query q = s.createQuery(query);

            if (params != null && params.containsKey("page")) {
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
    }

    @Override
    public Thesis addOrUpdate(Thesis thesis) {
        Session s = factory.getObject().getCurrentSession();
        if (thesis.getId() == null) {
            s.persist(thesis);
        } else {
            s.merge(thesis);
        }
        return thesis;
    }

    @Override
    public Thesis get(Map<String, String> params) {
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
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(builder.equal(root.get("id"), id));
            }
        }

        query.select(root).distinct(true);
        query.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(query);

        try {
            return (Thesis) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        Thesis thesis = get(new HashMap<>(Map.of("id", String.valueOf(id))));
        for (Student student : thesis.getStudents()) {
            student.getTheses().remove(thesis);
        }

        for (Lecturer lecturer : thesis.getLecturers()) {
            lecturer.getThesesSupervisors().remove(thesis);
        }
        s.remove(thesis);
    }

    @Override
    public int countThesesByStudentAndNotThisThesis(Integer studentUserId, Integer thesisIdToExclude) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Thesis> thesisRoot = query.from(Thesis.class);

        // Giả sử Thesis entity có một trường: private Set<User> students;
        Join<Thesis, User> studentJoin = thesisRoot.joinSet("students", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        // studentUserId là ID của User Pojo
        predicates.add(builder.equal(studentJoin.get("id"), studentUserId));

        if (thesisIdToExclude != null) {
            predicates.add(builder.notEqual(thesisRoot.get("id"), thesisIdToExclude));
        }

        query.select(builder.count(thesisRoot));
        query.where(predicates.toArray(new Predicate[0]));

        Long count = session.createQuery(query).getSingleResult();
        return count != null ? Integer.parseInt(count.toString()) : 0;
    }

    @Override
    public List<Thesis> getByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Thesis> query = builder.createQuery(Thesis.class);
        Root<Thesis> root = query.from(Thesis.class);
        query.select(root);
        query.where(root.get("id").in(ids));
        query.distinct(true); // Quan trọng nếu các fetch gây ra duplicate rows
        return s.createQuery(query).getResultList();
    }
}
