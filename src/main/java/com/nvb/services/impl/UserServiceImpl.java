/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvb.dto.UserDTO;
import com.nvb.pojo.User;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Admin;
import com.nvb.repositories.UserRepository;
import com.nvb.services.AcademicsStaffService;
import com.nvb.services.AdminService;
import com.nvb.services.LecturerService;
import com.nvb.services.StudentService;
import com.nvb.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nguyenvanbao
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AcademicsStaffService academicsStaffService;
    @Autowired
    private LecturerService lecturerService;
    @Autowired
    private StudentService studentService;

    @Override
    public User getUser(Map<String, String> params) {
        return userRepository.getUser(params);
    }

    @Override
    @Transactional
    public User addUser(UserDTO userDto, MultipartFile avatar) {
        User u = new User();
        u.setFirstName(userDto.getFirstName());
        u.setLastName(userDto.getLastName());
        u.setEmail(userDto.getEmail());
        u.setPhone(userDto.getPhone());
        u.setUsername(userDto.getUsername());
        u.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        u.setRole(userDto.getRole());
        
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Error uploading avatar: " + ex.getMessage());
            }
        }
        
        switch (userDto.getRole()) {
            case "ROLE_ADMIN":
                u.setAdmin(adminService.prepareAdmin(u, userDto));
                break;
            case "ROLE_ACADEMICSTAFF":
                u.setAcademicStaff(academicsStaffService.prepareAcademicStaff(u, userDto));
                break;
            case "ROLE_LECTURER":
                u.setLecturer(lecturerService.prepareLecturer(u, userDto));
                break;
            case "ROLE_STUDENT":
                u.setStudent(studentService.prepareStudent(u, userDto));
                break;
            default:
                break;
        }
        
        return this.userRepository.addUser(u);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return userRepository.authenticate(username, password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUser(Map.of("username", username));
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));
        
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return this.userRepository.getUsers(params);
    }

    @Override
    public void deleteUser(int id){
        User u = this.getUser(Map.of("id", String.valueOf(id)));
        if (u == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        
        userRepository.deleteUser(u);
    }
    
}
