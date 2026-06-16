package com.universite.apirest.controller;

import com.universite.apirest.entity.Utilisateur;
import com.universite.apirest.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            Map<String, Object> response = authService.login(username, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = Map.of("message", "Identifiants invalides");
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        try {
            String username = registerRequest.get("username");
            String password = registerRequest.get("password");
            String email = registerRequest.get("email");
            String role = registerRequest.getOrDefault("role", "ETUDIANT");
            
            Utilisateur utilisateur = authService.register(username, password, email, role);
            Map<String, String> response = Map.of(
                "message", "Utilisateur créé avec succès",
                "username", utilisateur.getUsername()
            );
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = Map.of("message", e.getMessage());
            return ResponseEntity.status(400).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        Map<String, String> response = Map.of("message", "Déconnexion réussie");
        return ResponseEntity.ok(response);
    }
}
