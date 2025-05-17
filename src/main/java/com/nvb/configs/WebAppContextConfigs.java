/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.configs;

import com.nvb.dto.EvaluationCriteriaCollectionDTO;
import com.nvb.dto.EvaluationCriteriaDTO;
import com.nvb.dto.UserDTO;
import com.nvb.formatter.EvaluationCriteriaDTOFormatter;
import com.nvb.formatter.EvaluationCriteriaFormatter;
import com.nvb.pojo.User;
import com.nvb.validators.EvaluationCriteriaCollectionValidator;
import com.nvb.validators.EvaluationCriteriaValidator;
import com.nvb.validators.UserValidator;
import com.nvb.validators.WebAppValidator;
import java.util.HashSet;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author nguyenvanbao
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.nvb.controllers",
    "com.nvb.repositories",
    "com.nvb.services",
    "com.nvb.validators",
    "com.nvb.exceptions",
    "com.nvb.thesismanagementbackend",
    "com.nvb.utils",
    "com.nvb.filters",
    "com.nvb.formatter"
})
public class WebAppContextConfigs implements WebMvcConfigurer {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private EvaluationCriteriaDTOFormatter evaluationCriteriaDTOFormatter;
    
    @Autowired
    private EvaluationCriteriaFormatter evaluationCriteriaFormatter;
    
    @Autowired
    private EvaluationCriteriaCollectionValidator evaluationCriteriaCollectionValidator;
    
    @Autowired
    private EvaluationCriteriaValidator evaluationCriteriaValidator;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public WebAppValidator userWebAppValidator() {
        Set<Validator> springValidators = new HashSet<>();
        springValidators.add(userValidator);
        WebAppValidator webAppValidator = new WebAppValidator();
        webAppValidator.setSupportedClass(UserDTO.class);
        webAppValidator.setSpringValidators(springValidators);
        return webAppValidator;
    }
    
    @Bean
    public WebAppValidator evaluationCriteriaCollectionWebAppValidator() {
        Set<Validator> springValidators = new HashSet<>();
        springValidators.add(evaluationCriteriaCollectionValidator);
        WebAppValidator webAppValidator = new WebAppValidator();
        webAppValidator.setSupportedClass(EvaluationCriteriaCollectionDTO.class);
        webAppValidator.setSpringValidators(springValidators);
        return webAppValidator;
    }
    
    @Bean
    public WebAppValidator evaluationCriteriaWebAppValidator() {
        Set<Validator> springValidators = new HashSet<>();
        springValidators.add(evaluationCriteriaValidator);
        WebAppValidator webAppValidator = new WebAppValidator();
        webAppValidator.setSupportedClass(EvaluationCriteriaDTO.class);
        webAppValidator.setSpringValidators(springValidators);
        return webAppValidator;
    }


    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource); // Kết nối với MessageSource
        return validatorFactoryBean;
    }

    @Override
    public LocalValidatorFactoryBean getValidator() {
        return localValidatorFactoryBean(); // Cung cấp validator này cho Spring MVC
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(User.class, UserDTO.class).addMappings(mapper -> {
            mapper.skip(UserDTO::setPassword);
        });
        return modelMapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(evaluationCriteriaDTOFormatter);
        registry.addFormatter(evaluationCriteriaFormatter);
    }
}