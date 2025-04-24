package com.management.management.service;

import com.management.management.dtos.LoginUserDto;
import com.management.management.dtos.RegisterUserDto;
import com.management.management.dtos.SetRoleDto;
import com.management.management.response.GenericResponse;

public interface UserService {
    GenericResponse<?> registerUser(RegisterUserDto registerUserDto);

    GenericResponse<?> loginUser(LoginUserDto loginUserDto);

    GenericResponse<?> getUserByEmail(String email);

    GenericResponse<?> setRole(SetRoleDto setRoleDto);

    GenericResponse<?> getCurrentUser(String email);
}
