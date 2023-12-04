package com.hirshi001.springbootmicroservices.accounts.service;

import com.hirshi001.springbootmicroservices.accounts.dto.UserInfoDto;
import com.hirshi001.springbootmicroservices.accounts.dto.UserRegistrationDto;
import com.hirshi001.springbootmicroservices.accounts.entity.User;
import com.hirshi001.springbootmicroservices.accounts.exception.IncorrectPasswordException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(UserRegistrationDto userDto);

    boolean updateUser(Object obj);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Integer id);

    Optional<User> findUserByUsername(String username);

    List<UserInfoDto> findAllUsers();

    List<String> findAllRoles();

    boolean authenticate(String username, String password) throws UsernameNotFoundException, BadCredentialsException;
}
