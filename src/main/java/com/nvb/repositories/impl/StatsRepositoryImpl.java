/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.repositories.impl;

import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.Major;
import com.nvb.pojo.Student;
import com.nvb.pojo.Thesis;
import com.nvb.repositories.StatsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
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
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> statsThesisScoresByYear(int year) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<EvaluationFinalScore> root = q.from(EvaluationFinalScore.class);
        Join<EvaluationFinalScore, Thesis> thesisJoin = root.join("thesis", JoinType.INNER);
        q.multiselect(thesisJoin.get("id"), thesisJoin.get("title"), root.get("averageScore"));
        q.where(b.equal(b.function("YEAR", Integer.class, thesisJoin.get("createdAt")), year));
        q.groupBy(thesisJoin.get("id"), thesisJoin.get("title"), root.get("averageScore"));

        List<Object[]> result = s.createQuery(q).getResultList();

        CriteriaQuery<Object[]> totalQuery = b.createQuery(Object[].class);
        Root<EvaluationFinalScore> rootTotal = totalQuery.from(EvaluationFinalScore.class);
        Join<EvaluationFinalScore, Thesis> thesisJoinTotal = rootTotal.join("thesis", JoinType.INNER);

        totalQuery.multiselect(
                b.literal("Tổng cộng"), 
                b.countDistinct(thesisJoinTotal.get("id")),
                b.avg(rootTotal.get("averageScore"))
        );
        totalQuery.where(b.equal(b.function("YEAR", Integer.class, thesisJoinTotal.get("createdAt")), year));

        Object[] totalResult = s.createQuery(totalQuery).uniqueResult();

        result.add(totalResult);

        return result;
    }

    @Override
    public List<Object[]> statsThesisParticipationByMajor(int year) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Truy vấn lấy số lượng khóa luận theo ngành
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<EvaluationFinalScore> root = q.from(EvaluationFinalScore.class);
        Join<EvaluationFinalScore, Thesis> thesisJoin = root.join("thesis", JoinType.INNER);
        Join<Thesis, Student> studentJoin = thesisJoin.join("students", JoinType.INNER);
        Join<Student, Major> majorJoin = studentJoin.join("major", JoinType.INNER);

        q.multiselect(
                majorJoin.get("name"),
                b.countDistinct(thesisJoin.get("id"))
        );
        q.where(b.equal(b.function("YEAR", Integer.class, thesisJoin.get("createdAt")), year));
        q.groupBy(majorJoin.get("name"));

        List<Object[]> result = s.createQuery(q).getResultList();

        // Truy vấn lấy tổng số khóa luận cho năm đó
        CriteriaQuery<Object[]> totalQuery = b.createQuery(Object[].class);
        Root<EvaluationFinalScore> rootTotal = totalQuery.from(EvaluationFinalScore.class);
        Join<EvaluationFinalScore, Thesis> thesisJoinTotal = rootTotal.join("thesis", JoinType.INNER);

        totalQuery.multiselect(
                b.literal("Tổng cộng"), // Gán nhãn tổng cộng
                b.countDistinct(thesisJoinTotal.get("id"))
        );
        totalQuery.where(b.equal(b.function("YEAR", Integer.class, thesisJoinTotal.get("createdAt")), year));

        Object[] totalResult = s.createQuery(totalQuery).uniqueResult();

        // Thêm dòng tổng vào cuối danh sách kết quả
        result.add(totalResult);

        return result;
    }

}
