package com.management.management.service;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.LoginResponseDto;
import com.management.management.dtos.LoginUserDto;
import com.management.management.dtos.RegisterUserDto;
import com.management.management.dtos.SetRoleDto;
import com.management.management.entity.User;
import com.management.management.repository.UserRepository;
import com.management.management.utility.GenericResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public GenericResponse<?> registerUser(RegisterUserDto registerUserDto) {
        try {
            User existingUser = userRepository.findByEmail(registerUserDto.getEmail());
            if (existingUser != null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "User with this email already exists", null);
            }

            User user = new User(registerUserDto.getEmail(), registerUserDto.getName());
            user = userRepository.save(user);
            userCredentialsService.registerUserCredentials(user, registerUserDto.getPassword());
            return new GenericResponse<>(HttpConstants.SUCCESS, "User created successfully", null);
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Error during registration: " + e.getMessage(), null);
        }
    }

    public GenericResponse<?> loginUser(LoginUserDto loginUserDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
            );

            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(loginUserDto.getEmail());
                if (user == null) {
                    return new GenericResponse<>(HttpConstants.FAILURE, "User not found", null);
                }

                String token = jwtService.generateToken(user);
                LoginResponseDto responseDto = new LoginResponseDto();
                responseDto.setToken(token);
                responseDto.setEmail(user.getEmail());
                responseDto.setName(user.getName());
                responseDto.setRoles(user.getRoles());
                return new GenericResponse<>(HttpConstants.SUCCESS, "Login successful", responseDto);
            } else {
                return new GenericResponse<>(HttpConstants.FAILURE, "Invalid credentials", null);
            }
        } catch (BadCredentialsException e) {
            log.warn("Bad credentials for user {}", loginUserDto.getEmail());
            return new GenericResponse<>(HttpConstants.FAILURE, "Invalid email or password", null);
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage(), e);
            return new GenericResponse<>(HttpConstants.FAILURE, "Authentication failed: " + e.getMessage(), null);
        }
    }

    public GenericResponse<?> getUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);

            if (user == null) {
                return new GenericResponse<>(
                        HttpConstants.FAILURE,
                        "User not found",
                        null
                );
            }

            return new GenericResponse<>(
                    HttpConstants.SUCCESS,
                    "User retrieved successfully",
                    user
            );
        } catch (Exception e) {
            return new GenericResponse<>(
                    HttpConstants.FAILURE,
                    e.getMessage(),
                    null
            );
        }
    }

    public GenericResponse<?> setRole(@Valid SetRoleDto setRoleDto) {
        try {
            User user = userRepository.findByEmail(setRoleDto.getEmail());
            if (user == null) {
                return new GenericResponse<>(HttpConstants.FAILURE, "User not found", null);
            }
            if (user.getRoles().contains(setRoleDto.getRole())) {
                return new GenericResponse<>(HttpConstants.FAILURE, "User already has this role", null);
            }
            user.getRoles().add(setRoleDto.getRole());
            user = userRepository.save(user);
            return new GenericResponse<>(HttpConstants.SUCCESS, "Role updated successfully", null);
        } catch (Exception e) {
            return new GenericResponse<>(HttpConstants.FAILURE, "Error setting role: " + e.getMessage(), null);
        }
    }
}