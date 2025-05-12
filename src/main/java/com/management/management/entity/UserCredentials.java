package com.management.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "userCredentials")
public class UserCredentials {

    @DBRef
    private User user;

    @NonNull
    private String password;
}
