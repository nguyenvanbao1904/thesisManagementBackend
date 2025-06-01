/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvb.filters.JwtFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author nguyenvanbao
 */
@Configuration
@EnableTransactionManagement
@EnableWebSecurity
@ComponentScan(basePackages = {
    "com.nvb.controllers",
    "com.nvb.repositories",
    "com.nvb.services",
    "com.nvb.filters",
    "com.nvb.utils",
    "com.nvb.components"
})
@PropertySource("classpath:application.properties")
public class SpringSecurityConfigs {
    
    @Autowired
    private Environment env;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
    
    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOrigins(List.of("http://localhost:3000/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
    
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", env.getProperty("cloudinary.cloud_name"),
                        "api_key", env.getProperty("cloudinary.api_key"),
                        "api_secret", env.getProperty("cloudinary.api_secret"),
                        "secure", true));
        return cloudinary;
    }
    
    @Autowired
    private JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
            Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(c -> c.disable())
                .authorizeHttpRequests(requests -> requests
                .requestMatchers("/").hasAnyRole("ADMIN", "ACADEMICSTAFF")
                .requestMatchers("/**").hasRole("ADMIN")
                // 📘 Theses
                .requestMatchers(HttpMethod.DELETE, "/api/theses/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/theses/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/theses/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers("/api/theses/**").hasAnyRole("ACADEMICSTAFF", "ADMIN", "LECTURER")
                // 📘 Committees
                .requestMatchers(HttpMethod.DELETE, "/api/committees/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/committees/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/committees/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/committees/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers("/api/committees/**").hasAnyRole("LECTURER", "ACADEMICSTAFF", "ADMIN")
                // 📘 Evaluation Criteria Collections
                .requestMatchers(HttpMethod.DELETE, "/api/evaluation_criteria_collections/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/evaluation_criteria_collections/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/evaluation_criteria_collections/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                .requestMatchers("/api/evaluation_criteria_collections/**").hasAnyRole("LECTURER", "ACADEMICSTAFF", "ADMIN")
                // 📘 Evaluation Criterias
                .requestMatchers("/api/evaluation_criterias/**").hasAnyRole("ACADEMICSTAFF", "ADMIN")
                // 📘 Majors
                .requestMatchers("/api/majors/**").hasRole("ADMIN")
                // 📘 Lecturers
                .requestMatchers("/api/lecturer/**").hasRole("LECTURER")
                // Evaluate
                .requestMatchers("/api/evaluations/**").hasRole("LECTURER")
                // 📘 Users – chỉ ADMIN được xóa
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "ACADEMICSTAFF")
                // 🌐 Public API – mặc định được phép
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login").permitAll());
        return http.build();
    }
}
