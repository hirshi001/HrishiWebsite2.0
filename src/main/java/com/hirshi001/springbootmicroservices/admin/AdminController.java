package com.hirshi001.springbootmicroservices.admin;

import com.hirshi001.springbootmicroservices.accounts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping({"/index", "/"})
    public String index() {
        return "admin/index";
    }

    @GetMapping("/sidebar")
    public String sidebar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("name", auth.getName());
        return "admin/sidebar";
    }

    @GetMapping("/editusers")
    public String editusers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("roles", userService.findAllRoles());
        return "admin/editusers";
    }

    @PostMapping("/editusers/save")
    public ResponseEntity<String> saveUsers(@RequestBody List<Object> payload) {
        List<Integer> failedIds = new ArrayList<>();
        List<Integer> successIds = new ArrayList<>();
        for(Object obj : payload) {
            String sid = (String) ((Map<String, Object>) obj).get("ID");
            int id = Integer.parseInt(sid);
            if(userService.updateUser(obj)) {
                successIds.add(id);
            } else {
                failedIds.add(id);
            }
        }

        if(failedIds.isEmpty()) {
            return ResponseEntity.ok("Successfully updated users: " + successIds);
        }
        return ResponseEntity.badRequest().body("Successfully updated users: " + successIds + "\nFailed to update users: " + failedIds);
    }
}
