package com.management.management.filters;

import com.management.management.Constants.HttpConstants;
import com.management.management.service.JwtService;
import com.management.management.service.UserDetailsServiceImpl;
import com.management.management.utility.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpConstants.AUTHORIZATION_HEADER);
        String token = null;
        String email = null;
        if(authHeader != null && authHeader.startsWith(HttpConstants.BEARER)) {
            token = JwtTokenUtil.removeBearer(authHeader);
            email = jwtService.extractEmail(token);
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = applicationContext.getBean(UserDetailsServiceImpl.class).loadUserByUsername(email);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
