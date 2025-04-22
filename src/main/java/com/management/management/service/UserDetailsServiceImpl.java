package com.management.management.service;

import com.management.management.entity.User;
import com.management.management.models.UserPrincipal;
import com.management.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + email);
        }
        String password = userCredentialsService.findByUser(user).getPassword();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                password,
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList())
        );
    }
}
