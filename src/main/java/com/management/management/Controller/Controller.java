package com.management.management.Controller;

import com.management.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class Controller {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void registerUser(String email, String name, String password) {
        userService.registerUser(email, name, password);
    }
}
