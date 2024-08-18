package com.oseanchen.crudproject.dao;

import com.oseanchen.crudproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long> {
     Optional<Role> findByRoleName(String roleName);
}
