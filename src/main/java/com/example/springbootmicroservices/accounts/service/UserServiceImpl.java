package com.example.springbootmicroservices.accounts.service;

import com.example.springbootmicroservices.accounts.dto.UserDto;
import com.example.springbootmicroservices.accounts.entity.Role;
import com.example.springbootmicroservices.accounts.entity.User;
import com.example.springbootmicroservices.accounts.repository.RoleRepository;
import com.example.springbootmicroservices.accounts.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName(Role.ROLE_USER);
        if(role == null) {
            role = checkRoleExist(Role.ROLE_USER);
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    private Role checkRoleExist(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
