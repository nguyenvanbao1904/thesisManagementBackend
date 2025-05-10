/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nguyenvanbao
 */
public class UserDTO {
    private Integer id;
    @NotBlank(message = "{user.username.notnullMsg}")
    private String username;
    @NotBlank(message = "{user.password.notnullMsg}")
    @Size(min = 6, message = "{user.password.tooshortMsg}")
    private String password;
    @NotBlank(message = "{user.firstname.notnullMsg}")
    private String firstName;
    @NotBlank(message = "{user.lastname.notnullMsg}")
    private String lastName;
    @NotBlank(message = "{user.email.notnullMsg}")
    private String email;
    @NotBlank(message = "{user.phone.invalidMsg}")
    private String phone;
    private String avatarUrl;
    @NotNull(message = "{user.role.notnullMsg}")
    private String role;
    private Boolean isActive = true;
    @NotNull(message = "{user.avatar.notnullMsg}")
    private MultipartFile file;
    
    // Thông tin cho sinh viên
    private String studentId;
    private Integer majorId;
    
    // Thông tin cho giảng viên
    private String academicTitle;
    private String academicDegree;

    public UserDTO() {
    }

    public UserDTO(Integer id, String username, String password, String firstName, String lastName, String email, String phone, String avatarUrl, String role, MultipartFile file, String studentId, Integer majorId, String academicTitle, String academicDegree) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.file = file;
        this.studentId = studentId;
        this.majorId = majorId;
        this.academicTitle = academicTitle;
        this.academicDegree = academicDegree;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", role=" + role + '}';
    }
}
