package com.management.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
public class UserCredentials {

    @DBRef
    private User user;

    @NonNull
    private String password;
}
