package com.management.management.service;

import com.management.management.entity.User;
import com.management.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;

    public void registerUser(String email, String name, String password) {
        final User user = userRepository.save(new User(email, name));
        userCredentialsService.registerUserCredentials(user, password);
    }
}
