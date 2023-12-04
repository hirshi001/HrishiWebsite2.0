package com.hirshi001.springbootmicroservices.accounts.security;

import com.hirshi001.springbootmicroservices.accounts.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String id = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("id: " + id);
        if(true)
            throw new OAuth2AuthenticationException("Not implemented yet");
        return null;
    }
}
