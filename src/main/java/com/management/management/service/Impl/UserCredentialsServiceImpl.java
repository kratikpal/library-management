package com.management.management.service.Impl;

import com.management.management.entity.User;
import com.management.management.entity.UserCredentials;
import com.management.management.repository.UserCredentialsRepository;
import com.management.management.service.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    public void registerUserCredentials(User user, String password) {
        password = passwordEncoder.encode(password);
        final UserCredentials userCredentials = new UserCredentials(user, password);
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    public UserCredentials findByUser(User user) {
        return userCredentialsRepository.findByUser(user);
    }
}
