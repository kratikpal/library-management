package com.management.management.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetRoleDto {
    @NotNull(message = "Role is required")
    private String role;
    @NotNull(message = "Email is required")
    private String email;
}
