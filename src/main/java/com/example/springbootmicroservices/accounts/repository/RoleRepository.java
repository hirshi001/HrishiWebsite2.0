package com.example.springbootmicroservices.accounts.repository;

import com.example.springbootmicroservices.accounts.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roles")
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);

}
