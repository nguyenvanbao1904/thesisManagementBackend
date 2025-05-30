/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvb.dto.UserDTO;
import com.nvb.pojo.User;
import com.nvb.pojo.Student;
import com.nvb.pojo.Lecturer;
import com.nvb.repositories.UserRepository;
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
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public UserDTO get(Map<String, String> params) {
        User user = userRepository.get(params);
        return user != null ? toDTO(user, true) : null;
    }

    @Override
    public UserDTO addOrUpdate(UserDTO userDto, MultipartFile avatar) {
        User u;
        if (userDto.getId() != null) {
            u = userRepository.get(Map.of("id", String.valueOf(userDto.getId())));
            if (!userDto.getRole().equals(String.format("ROLE_%s", u.getClass().getSimpleName().toUpperCase()))) {
                throw new IllegalArgumentException("Changing user role is not allowed through this update method. Please delete and recreate the user if role change is necessary.");
            }
            if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
                u.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
            }
        } else {
            u = new User();
            u.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        }
        u.setFirstName(userDto.getFirstName());
        u.setLastName(userDto.getLastName());
        u.setEmail(userDto.getEmail());
        u.setPhone(userDto.getPhone().isBlank() ? null : userDto.getPhone());
        u.setUsername(userDto.getUsername());
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (userDto.getAvatarUrl() != null && !userDto.getAvatarUrl().isBlank()) {
            u.setAvatarUrl(userDto.getAvatarUrl());
        }
        return this.toDTO(this.userRepository.addOrUpdate(u), false);
    }

    @Override
    public UserDTO authenticate(String username, String password) {
        User user = userRepository.authenticate(username, password);
        return user != null ? toDTO(user, false) : null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.userRepository.get(Map.of("username", username));
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getUserType()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public List<UserDTO> getAll(Map<String, String> params) {
        return this.getAll(params, false, false);
    }

    @Override
    public List<UserDTO> getAll(Map<String, String> params, boolean pagination) {
        return this.getAll(params, pagination, false);
    }

    @Override
    public List<UserDTO> getAll(Map<String, String> params, boolean pagination, boolean details) {
        List<User> users = this.userRepository.getAll(params, pagination, details);
        return users.stream().map(user -> toDTO(user, false)).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void delete(int id) {
        User u = this.userRepository.get(Map.of("id", String.valueOf(id)));
        if (u == null) {
            throw new IllegalArgumentException("Invalid user");
        }

        userRepository.delete(u);
    }

    private UserDTO toDTO(User user, boolean details) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setUsername(user.getUsername());
        userDto.setAvatarUrl(user.getAvatarUrl());
        userDto.setRole(user.getUserType());
        if (user instanceof Student) {
            Student student = (Student) user;
            userDto.setStudentId(student.getStudentId());
            if (details && student.getMajor() != null) {
                userDto.setMajorId(student.getMajor().getId());
                userDto.setMajorName(student.getMajor().getName());
            }
        } else if (user instanceof Lecturer) {
            Lecturer lecturer = (Lecturer) user;
            userDto.setAcademicDegree(lecturer.getAcademicDegree());
            userDto.setAcademicTitle(lecturer.getAcademicTitle());
        }
        return userDto;
    }
}
