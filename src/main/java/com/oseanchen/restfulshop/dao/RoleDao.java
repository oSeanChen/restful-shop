package com.oseanchen.restfulshop.dao;

import com.oseanchen.restfulshop.model.Role;
import com.oseanchen.restfulshop.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long> {
     Optional<Role> findByRoleName(UserRole roleName);
}
