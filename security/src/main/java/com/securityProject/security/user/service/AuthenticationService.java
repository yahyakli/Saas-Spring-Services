package com.securityProject.security.user.service;


import com.securityProject.security.user.config.JwtService;
import com.securityProject.security.user.controller.AuthenticationRequest;
import com.securityProject.security.user.controller.AuthenticationResponse;
import com.securityProject.security.user.controller.RegisterRequest;
import com.securityProject.security.user.model.Role;
import com.securityProject.security.user.model.User;
import com.securityProject.security.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public List<User> getAllUsers (){
        return repository.findAll();
    }

    public User getUser (String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
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
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse updateUser(String id, RegisterRequest request) {
        var user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(request.getEmail());
        user.setName(request.getName());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void deleteUser(String id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        repository.delete(user);
    }
}
