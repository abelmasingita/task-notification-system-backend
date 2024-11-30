package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.dto.AuthResponseDto;
import com.datacentrix.notificationsystem.dto.LoginDto;
import com.datacentrix.notificationsystem.entity.User;
import com.datacentrix.notificationsystem.exception.AuthenticationException;
import com.datacentrix.notificationsystem.helper.JwtTokenProvider;
import com.datacentrix.notificationsystem.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> login(LoginDto loginDto)
    {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            String userId = getUserIdByUsername(loginDto.getUsername());

            AuthResponseDto res = new AuthResponseDto();
            res.setAccessToken(jwt);
            res.setUserId(userId);

            return ResponseEntity.ok(res);
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

    private String getUserIdByUsername(String username) {
        // Query the UserRepository to get the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found with username: " + username));

        // Return the userId (assuming User has an id field)
        return user.getId().toString();
    }
}
