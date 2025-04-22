package com.management.management.service;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.LoginUserDto;
import com.management.management.dtos.RegisterUserDto;
import com.management.management.entity.User;
import com.management.management.entity.UserCredentials;
import com.management.management.repository.UserRepository;
import com.management.management.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;

    public GenericResponse<?> registerUser(RegisterUserDto registerUserDto) {
        try {
            final boolean userExists = userRepository.findByEmail(registerUserDto.getEmail()) != null;
            if (userExists) {
                return new GenericResponse<>(HttpConstants.Failure, "User already exists", null);
            }
            final User user = userRepository.save(new User(registerUserDto.getEmail(), registerUserDto.getName()));
            userCredentialsService.registerUserCredentials(user, registerUserDto.getPassword());
            return new GenericResponse<>(HttpConstants.SUCCESS, "User created", null);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public GenericResponse<?> loginUser(LoginUserDto loginUserDto){
        try {
            final User user = userRepository.findByEmail(loginUserDto.getEmail());
            if (user == null) {
                return new GenericResponse<>(HttpConstants.Failure, "User does not exist", null);
            }
            final UserCredentials userCredentials = userCredentialsService.findByUser(user);
            if (userCredentials.getPassword().equals(loginUserDto.getPassword())) {
                return new GenericResponse<>(HttpConstants.SUCCESS, "User logged in", user);
            }
            return new GenericResponse<>(HttpConstants.Failure, "Invalid credentials", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
