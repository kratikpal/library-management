package com.management.management.service;

import com.management.management.entity.User;
import com.management.management.entity.UserCredentials;
import com.management.management.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService {
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;


    public void registerUserCredentials(User user, String password) {
        final UserCredentials userCredentials = new UserCredentials(user, password);
        userCredentialsRepository.save(userCredentials);
    }

    public UserCredentials findByUser(User user) {
        return userCredentialsRepository.findByUser(user);
    }
}
