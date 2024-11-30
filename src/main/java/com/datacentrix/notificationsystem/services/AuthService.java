package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.dto.AuthResponseDto;
import com.datacentrix.notificationsystem.dto.LoginDto;
import com.datacentrix.notificationsystem.exception.AuthenticationException;
import com.datacentrix.notificationsystem.helper.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> login(LoginDto loginDto)
    {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new AuthResponseDto(jwt));
        } catch (BadCredentialsException e) {
            // Throw custom exception for authentication failure
            throw new AuthenticationException("Invalid username or password", e);
        } catch (Exception e) {
            // Catch other general exceptions
            throw new AuthenticationException("An error occurred during authentication", e);
        }

    }

    public void logout() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                authentication.setAuthenticated(false);
            }
        }catch (Exception e) {
            // Catch other general exceptions
            throw new AuthenticationException("An error occurred during authentication", e);
        }
    }
}
