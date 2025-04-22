package com.management.management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Password is required")
    private String password;
}
