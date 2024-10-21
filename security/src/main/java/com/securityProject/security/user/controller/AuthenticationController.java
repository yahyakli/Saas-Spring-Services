package com.securityProject.security.user.controller;


import com.securityProject.security.user.model.User;
import com.securityProject.security.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/users")
    public List<User> getAllUsers (){
        return service.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser (@PathVariable String id){
        return service.getUser(id);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return  ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return  ResponseEntity.ok(service.authenticate(request));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<AuthenticationResponse> updateUser(
            @PathVariable String id,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
