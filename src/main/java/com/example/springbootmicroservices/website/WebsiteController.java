package com.example.springbootmicroservices.website;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebsiteController {


    @GetMapping({"/index", "/"})
    public String index() {
        return "website/index";
    }


    @GetMapping({"/sidebar", "/sidebar"})
    public String sidebar() {
        return "website/sidebar";
    }

    @GetMapping({"/projects", "/projects"})
    public String projects() {
        return "website/projects";
    }

    @GetMapping("/login")
    public String login(){
        return "website/login";
    }


    @GetMapping("/signup")
    public String signup(){
        return "website/signup";
    }



}
