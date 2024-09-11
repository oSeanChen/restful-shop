package com.oseanchen.restfulshop.dao;

import com.oseanchen.restfulshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
