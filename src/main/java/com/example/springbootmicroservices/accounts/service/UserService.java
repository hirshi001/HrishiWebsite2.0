package com.example.springbootmicroservices.accounts.service;

import com.example.springbootmicroservices.accounts.dto.UserDto;
import com.example.springbootmicroservices.accounts.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
