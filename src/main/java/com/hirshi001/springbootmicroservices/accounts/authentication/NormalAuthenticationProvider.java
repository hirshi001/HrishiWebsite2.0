package com.hirshi001.springbootmicroservices.accounts.authentication;

import com.hirshi001.springbootmicroservices.accounts.entity.User;
import com.hirshi001.springbootmicroservices.accounts.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NormalAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    public NormalAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        if(!userService.authenticate(username, password)){
            throw new BadCredentialsException("Incorrect Credentials");
        }
        System.out.println("User authenticated");


        List<GrantedAuthority> authorities = new ArrayList<>(user.getRoles());
        authorities.addAll(user.getAuthorities());
        System.out.println("Authorities: " + authorities);
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
