package com.hirshi001.springbootmicroservices.accounts.repository;

import com.hirshi001.springbootmicroservices.accounts.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authority, Integer> {

    Authority findByAuthority(String authority);

    Authority findById(int id);
}
