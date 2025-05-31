/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services;

import jakarta.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class CommitteeSchedulerService {

    @Autowired
    private CommitteeService committeeService;

    @PostConstruct
    public void handleMissedEvents() {
        System.out.println("Checking for missed committee events...");
        Date now = new Date();

        try {
            // Activate committees cần được mở
            activateMissedCommittees(now);

            // Lock committees cần được khóa
            lockOverdueCommittees(now);

            System.out.println("Missed events check completed");
        } catch (Exception e) {
            System.err.println("Error in missed events check: " + e.getMessage());
        }
    }

    // Chạy mỗi phút để check status
    @Scheduled(fixedRate = 60000) // 60 giây = 1 phút
    public void updateCommitteeStatus() {
        System.out.println("Running scheduled committee status update...");
        Date now = new Date();

        try {
            // 1. Activate committees 30 phút trước defense
            activateCommittees(now);

            // 2. Lock committees sau 2 ngày defense
            lockCommittees(now);

        } catch (Exception e) {
            System.err.println("Error in scheduled task: " + e.getMessage());
        }
    }

    
    private void activateCommittees(Date now) {
        // Tính thời gian 30 phút từ bây giờ
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, 30);
        Date targetTime = cal.getTime();

        int count = committeeService.activateCommitteesBeforeDefense(now, targetTime);
        if (count > 0) {
            System.out.println("Activated " + count + " committees");
        }
    }

    private void lockCommittees(Date now) {
        // Tính thời gian 2 ngày trước
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, -2);
        Date lockTime = cal.getTime();

        int count = committeeService.lockCommitteesAfterDefense(lockTime);
        if (count > 0) {
            System.out.println("Locked " + count + " committees");
        }
    }

    // Xử lý missed events khi restart
    private void activateMissedCommittees(Date now) {
        // Tìm committees cần activate (đã quá giờ nhưng vẫn LOCKED)
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, 30);
        Date futureTime = cal.getTime();

        int count = committeeService.activateMissedCommittees(now, futureTime);
        if (count > 0) {
            System.out.println("Activated " + count + " missed committees");
        }
    }

    private void lockOverdueCommittees(Date now) {
        // Tìm committees cần lock (đã quá hạn nhưng vẫn ACTIVE)
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, -2);
        Date lockTime = cal.getTime();

        int count = committeeService.lockOverdueCommittees(lockTime);
        if (count > 0) {
            System.out.println("Locked " + count + " overdue committees");
        }
    }
}
