package com.spiegelberger.springit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spiegelberger.springit.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}