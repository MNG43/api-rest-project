package com.universite.apirest.security;

import com.universite.apirest.entity.Utilisateur;
import com.universite.apirest.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UserDetailsServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(utilisateur.getUsername())
                .password(utilisateur.getPassword())
                .roles(utilisateur.getRole().name())
                .disabled(!utilisateur.isActif())
                .build();
    }
}
