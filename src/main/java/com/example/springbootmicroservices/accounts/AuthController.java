package com.example.springbootmicroservices.accounts;

import com.example.springbootmicroservices.accounts.dto.UserDto;
import com.example.springbootmicroservices.accounts.entity.User;
import com.example.springbootmicroservices.accounts.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthController {


    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/accounts/register")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", "Email already exists",
                    "There is already an account registered with the same email");
        }
        System.out.println(userDto);

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            System.out.println("Error in registration");
            return "accounts/register";
        }

        System.out.println("Saving user");
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/accounts/login")
    public String login() {
        return "accounts/login";
    }

    // handler method to handle list of users
    @GetMapping("/accounts/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "accounts/users";
    }

}
