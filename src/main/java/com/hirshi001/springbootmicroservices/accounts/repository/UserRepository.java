package com.hirshi001.springbootmicroservices.accounts.repository;

import com.hirshi001.springbootmicroservices.accounts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);

    List<User> findUsersByName(String name);

    User findUserByUsername(String username);

    User findUserById(Integer id);

}
