package com.hirshi001.springbootmicroservices.accounts.service;

import com.google.common.collect.Sets;
import com.hirshi001.springbootmicroservices.accounts.dto.UserInfoDto;
import com.hirshi001.springbootmicroservices.accounts.dto.UserRegistrationDto;
import com.hirshi001.springbootmicroservices.accounts.entity.Role;
import com.hirshi001.springbootmicroservices.accounts.entity.User;
import com.hirshi001.springbootmicroservices.accounts.exception.IncorrectPasswordException;
import com.hirshi001.springbootmicroservices.accounts.repository.RoleRepository;
import com.hirshi001.springbootmicroservices.accounts.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private SecureRandom secureRandom;
    public static final int TOKEN_LENGTH = 64;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public void saveUser(UserRegistrationDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName(Role.USER);
        if (role == null) {
            role = checkRoleExist(Role.USER);
        }
        user.setRoles(Sets.newHashSet(role));
        user = userRepository.save(user);

        role.getUsers().add(user);
        roleRepository.save(role);
    }

    @Override
    public boolean updateUser(Object obj) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;

            String sId = (String) map.get("ID");
            int id = Integer.parseInt(sId);

            User user = userRepository.findUserById(id);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            String username = (String) map.get("Username");
            String email = (String) map.get("Email");

            user.setUsername(username);
            user.setEmail(email);

            List<Role> roles = roleRepository.findAll();
            for (Role role : roles) {
                if ((Boolean) map.get(role.getName())) {
                    user.getRoles().add(role);
                   // role.getUsers().add(user);
                } else {
                    user.getRoles().remove(role);
                   // role.getUsers().remove(user);
                }
            }

            userRepository.save(user);
            // roleRepository.saveAll(roles);
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findUserByEmail(email));
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findUserByUsername(username));
    }

    @Override
    public List<UserInfoDto> findAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserInfoDto).collect(Collectors.toList());
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (checkPassword(password, user.getPassword())) {
            return true;
        }
        throw new BadCredentialsException("Password or email is incorrect.");
    }

    @Override
    public List<String> findAllRoles() {
        return roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
    }

    private boolean checkPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    private String generateToken(int length) {
        byte[] token = new byte[length];
        secureRandom.nextBytes(token);
        return Arrays.toString(token);
    }


    private UserInfoDto mapToUserInfoDto(User user) {
        UserInfoDto userDto = new UserInfoDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        return userDto;
    }

    private Role checkRoleExist(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
