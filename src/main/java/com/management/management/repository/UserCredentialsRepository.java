package com.management.management.repository;

import com.management.management.entity.User;
import com.management.management.entity.UserCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCredentialsRepository extends MongoRepository<UserCredentials, String> {
    UserCredentials findByUser(User user);
}
