/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.thesismanagementbackend;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author nguyenvanbao
 */
@WebListener
public class MySQLCleanupListener implements ServletContextListener{
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
