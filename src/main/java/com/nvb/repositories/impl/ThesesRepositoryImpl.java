/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Student;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.ThesesRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public List<Thesis> getTheses(Map<String, String> params, boolean pagination) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Thesis> query = builder.createQuery(Thesis.class);
        Root<Thesis> root = query.from(Thesis.class);
        
        // Eager fetch related entities to avoid LazyInitializationException
        root.fetch("reviewerId", JoinType.LEFT);
        root.fetch("evaluationCriteriaCollectionId", JoinType.LEFT);
        root.fetch("committeeId", JoinType.LEFT);
        root.fetch("lecturers", JoinType.LEFT);
        root.fetch("students", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(builder.like(root.get("title"), String.format("%%%s%%", title)));
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
    public Thesis addOrUpdate(Thesis thesis) {
        Session s = factory.getObject().getCurrentSession();
        
        if (thesis.getId() == null) {
            // Lưu các ID của lecturer và student
            Set<Integer> lecturerIds = new HashSet<>();
            Set<Integer> studentIds = new HashSet<>();
            
            for (Lecturer lecturer : thesis.getLecturers()) {
                lecturerIds.add(lecturer.getId());
            }
            
            for (Student student : thesis.getStudents()) {
                studentIds.add(student.getId());
            }
            
            // Xóa tạm thời danh sách để tránh persist cascading
            thesis.setLecturers(new HashSet<>());
            thesis.setStudents(new HashSet<>());
            
            // Lưu thesis trước
            s.persist(thesis);
            
            // Thiết lập lại mối quan hệ với lecturer và student
            Set<Lecturer> managedLecturers = new HashSet<>();
            for (Integer lecturerId : lecturerIds) {
                Lecturer managedLecturer = s.get(Lecturer.class, lecturerId);
                if (managedLecturer != null) {
                    managedLecturers.add(managedLecturer);
                    // Thêm thesis vào collection của lecturer
                    managedLecturer.getThesesSupervisors().add(thesis);
                }
            }
            thesis.setLecturers(managedLecturers);
            
            Set<Student> managedStudents = new HashSet<>();
            for (Integer studentId : studentIds) {
                Student managedStudent = s.get(Student.class, studentId);
                if (managedStudent != null) {
                    managedStudents.add(managedStudent);
                    // Thêm thesis vào collection của student
                    managedStudent.gettheses().add(thesis);
                }
            }
            thesis.setStudents(managedStudents);
        } else {
            thesis = (Thesis) s.merge(thesis);
        }
        return thesis;
    }

    @Override
    public Thesis getThesis(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Thesis> query = builder.createQuery(Thesis.class);
        Root<Thesis> root = query.from(Thesis.class);
        
        // Eager fetch related entities to avoid LazyInitializationException
        root.fetch("reviewerId", JoinType.LEFT);
        root.fetch("evaluationCriteriaCollectionId", JoinType.LEFT);
        root.fetch("committeeId", JoinType.LEFT);
        root.fetch("lecturers", JoinType.LEFT);
        root.fetch("students", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(builder.equal(root.get("title"), title));
            }
            String id = params.get("id");
            if(id != null && !id.isEmpty()){
                predicates.add(builder.equal(root.get("id"), id));
            }
        }

        query.select(root).distinct(true);
        query.where(predicates.toArray(new Predicate[0]));
        Query q = s.createQuery(query);

        try{
            return (Thesis) q.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
    
    @Override
    public Lecturer getLecturerWithTheses(Integer lecturerId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Lecturer> query = builder.createQuery(Lecturer.class);
        Root<Lecturer> root = query.from(Lecturer.class);
        
        // Eager fetch theses to ensure the relationship is loaded
        root.fetch("thesesSupervisors", JoinType.LEFT);
        
        query.select(root);
        query.where(builder.equal(root.get("id"), lecturerId));
        
        try {
            return s.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public Student getStudentWithTheses(Integer studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Student> query = builder.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);
        
        // Eager fetch theses to ensure the relationship is loaded
        root.fetch("theses", JoinType.LEFT);
        
        query.select(root);
        query.where(builder.equal(root.get("id"), studentId));
        
        try {
            return s.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void deleteThesis(int id) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(getThesis(new HashMap<>(Map.of("id", String.valueOf(id)))));
    }
}
