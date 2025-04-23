package com.management.management.service;

import com.management.management.entity.User;
import com.management.management.entity.UserCredentials;

public interface UserCredentialsService {
    void registerUserCredentials(User user, String password);

    UserCredentials findByUser(User user);
}
