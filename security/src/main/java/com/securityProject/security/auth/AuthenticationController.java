package com.securityProject.security.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

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
            @PathVariable int id,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }

    // Delete user endpoint
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
