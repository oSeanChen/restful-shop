package com.oseanchen.crudproject.dao;

import com.oseanchen.crudproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {


}
