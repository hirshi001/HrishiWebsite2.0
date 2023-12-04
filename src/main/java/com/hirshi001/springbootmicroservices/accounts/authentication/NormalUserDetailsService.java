package com.hirshi001.springbootmicroservices.accounts.authentication;

import com.hirshi001.springbootmicroservices.accounts.entity.User;
import com.hirshi001.springbootmicroservices.accounts.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NormalUserDetailsService implements UserDetailsService {
    private final UserService userRepository;

    public NormalUserDetailsService(UserService userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return userOptional.get();
    }

}
