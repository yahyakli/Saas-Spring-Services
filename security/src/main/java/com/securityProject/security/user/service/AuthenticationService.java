package com.securityProject.security.user.service;

import com.securityProject.security.config.JwtService;
import com.securityProject.security.user.controller.AuthenticationRequest;
import com.securityProject.security.user.controller.AuthenticationResponse;
import com.securityProject.security.user.controller.RegisterRequest;
import com.securityProject.security.user.model.Role;
import com.securityProject.security.user.model.User;
import com.securityProject.security.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<User> getUser(String id) {
        User user = repository.findById(id)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .created_at(new Date())
                .updated_at(new Date())
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<AuthenticationResponse> updateUser(String id, RegisterRequest request) {
        var user = repository.findById(id)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        user.setEmail(request.getEmail());
        user.setName(request.getName());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
    }

    public ResponseEntity<String> deleteUser(String id) {
        var user = repository.findById(id)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        repository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    public Map<String, String> generateTokens(UserDetails userDetails) {
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        // Extract username from the refresh token
        String username = jwtService.extractUsername(refreshToken);

        // Load user from DB or user service
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Validate the refresh token
        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            // Generate new access token
            String newAccessToken = jwtService.generateToken(userDetails);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", refreshToken); // You can choose to refresh refreshToken as well
            return tokens;
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
