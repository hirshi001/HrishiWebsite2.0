package com.hirshi001.springbootmicroservices.accounts;

import com.hirshi001.springbootmicroservices.accounts.dto.UserInfoDto;
import com.hirshi001.springbootmicroservices.accounts.dto.UserLoginDto;
import com.hirshi001.springbootmicroservices.accounts.dto.UserRegistrationDto;
import com.hirshi001.springbootmicroservices.accounts.entity.Role;
import com.hirshi001.springbootmicroservices.accounts.entity.User;
import com.hirshi001.springbootmicroservices.accounts.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {


    public static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/accounts/register")
    public ResponseEntity<String> registration(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto) {

        Optional<User> existingUser = userService.findUserByEmail(userRegistrationDto.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("This email already exists");
        }
        LOGGER.info("User registration: {}", userRegistrationDto);
        userService.saveUser(userRegistrationDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "accounts/register";
    }

}
