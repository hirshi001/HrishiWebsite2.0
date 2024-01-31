package com.hirshi001.springbootmicroservices.website;

import com.hirshi001.springbootmicroservices.accounts.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebsiteController {

    @GetMapping({"/index", "/"})
    public String index(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ADMIN))) {
            return "admin/index";
        }
        return "website/index";
    }

    @GetMapping("/website/sidebar")
    public String sidebar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            model.addAttribute("name", "Guest");
        } else {
            model.addAttribute("name", auth.getName());
        }
        return "website/sidebar";
    }

    @GetMapping("/projects")
    public String projects() {
        return "website/projects";
    }


    @GetMapping("/login")
    public String login() {
        return "website/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "website/signup";
    }

}
