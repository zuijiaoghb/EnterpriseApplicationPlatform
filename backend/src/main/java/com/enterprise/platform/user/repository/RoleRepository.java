package com.enterprise.platform.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.platform.user.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);
}