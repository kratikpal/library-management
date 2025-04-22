package com.management.management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Password is required")
    private String password;
}
