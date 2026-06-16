package com.universite.apirest.service;

import com.universite.apirest.entity.Utilisateur;
import com.universite.apirest.repository.UtilisateurRepository;
import com.universite.apirest.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                      UserDetailsService userDetailsService,
                      JwtUtil jwtUtil,
                      UtilisateurRepository utilisateurRepository,
                      PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, Object> login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);
        
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", utilisateur.getUsername());
        response.put("email", utilisateur.getEmail());
        response.put("role", utilisateur.getRole().name());
        
        return response;
    }

    public Utilisateur register(String username, String password, String email, String role) {
        if (utilisateurRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé");
        }
        
        if (utilisateurRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(username);
        utilisateur.setPassword(passwordEncoder.encode(password));
        utilisateur.setEmail(email);
        utilisateur.setRole(com.universite.apirest.entity.Role.valueOf(role.toUpperCase()));
        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }

    public void logout(String token) {
        // Dans une implémentation plus avancée, on pourrait ajouter le token à une blacklist
        // Pour l'instant, le client doit simplement supprimer le token
    }
}
