package com.management.management.service;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.LoginUserDto;
import com.management.management.dtos.RegisterUserDto;
import com.management.management.entity.User;
import com.management.management.repository.UserRepository;
import com.management.management.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public GenericResponse<?> registerUser(RegisterUserDto registerUserDto) {
        try {
            final boolean userExists = userRepository.findByEmail(registerUserDto.getEmail()) != null;
            if (userExists) {
                return new GenericResponse<>(HttpConstants.FAILURE, "User already exists", null);
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
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));

            if (authentication.isAuthenticated()){
                final String token = jwtService.generateToken(loginUserDto.getEmail());
                return new GenericResponse<>(HttpConstants.SUCCESS, "User logged in", token);
            }else{
                return new GenericResponse<>(HttpConstants.FAILURE, "User not logged in", null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
