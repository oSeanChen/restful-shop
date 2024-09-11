package com.oseanchen.restfulshop.service;

import com.oseanchen.restfulshop.dao.RoleDao;
import com.oseanchen.restfulshop.dao.UserDao;
import com.oseanchen.restfulshop.dto.UserRequest;
import com.oseanchen.restfulshop.model.Role;
import com.oseanchen.restfulshop.model.User;
import com.oseanchen.restfulshop.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> getUsers() {
        return userDao.findAll();
    }

    public Optional<User> getUserByID(Integer id) {
        return userDao.findById(id);
    }

    @Transactional
    public User createUser(UserRequest userRequest) {
        Set<Role> roles = new HashSet<>();
        List<UserRole> roleList = Arrays.asList(UserRole.ROLE_BUYER, UserRole.ROLE_SELLER);
        for (UserRole role : roleList) {
            Role defaultRole = roleDao.findByRoleName(role)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            roles.add(defaultRole);
        }
        userRequest.setPassword(encoder.encode(userRequest.getPassword()));
        User user = convertToModel(userRequest);
        user.getRoles().addAll(roles);
        return userDao.save(user);
    }

    @Transactional
    public void deleteUserById(Integer id) {
        userDao.deleteById(id);
    }

    private User convertToModel(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return user;
    }
}
