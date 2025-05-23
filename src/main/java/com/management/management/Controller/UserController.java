package com.management.management.Controller;

import com.management.management.Constants.HttpConstants;
import com.management.management.dtos.LoginUserDto;
import com.management.management.dtos.RegisterUserDto;
import com.management.management.dtos.SetRoleDto;
import com.management.management.exception.HttpException;
import com.management.management.response.GenericResponse;
import com.management.management.service.EmailService;
import com.management.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public ResponseEntity<?> sendEmail() {
        try {
            emailService.sendRemainderEmail();
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @GetMapping()
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            GenericResponse<?> response = userService.getCurrentUser(email);

            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        try {
            GenericResponse<?> response = userService.registerUser(registerUserDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {
            GenericResponse<?> response = userService.loginUser(loginUserDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    @PostMapping("/set-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setRole(@Valid @RequestBody SetRoleDto setRoleDto) {
        try {
            GenericResponse<?> response = userService.setRole(setRoleDto);
            if (response.getStatus().equals(HttpConstants.SUCCESS)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new HttpException();
        }
    }
}