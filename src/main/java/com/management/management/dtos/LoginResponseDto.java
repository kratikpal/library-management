package com.management.management.dtos;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDto {
    private String token;
    private String email;
    private String name;
    private List<String> roles;
}