package com.universite.apirest.controller;

import com.universite.apirest.entity.Professeur;
import com.universite.apirest.service.ProfesseurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @GetMapping
    public ResponseEntity<List<Professeur>> getAllProfesseurs() {
        List<Professeur> professeurs = professeurService.getAllProfesseurs();
        return ResponseEntity.ok(professeurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfesseurById(@PathVariable Long id) {
        Optional<Professeur> professeur = professeurService.getProfesseurById(id);
        if (professeur.isPresent()) {
            return ResponseEntity.ok(professeur.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Professeur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProfesseur(@Valid @RequestBody Professeur professeur) {
        try {
            Professeur createdProfesseur = professeurService.createProfesseur(professeur);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Professeur créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfesseur(@PathVariable Long id, @Valid @RequestBody Professeur professeur) {
        try {
            Professeur updatedProfesseur = professeurService.updateProfesseur(id, professeur);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Professeur modifié");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfesseur(@PathVariable Long id) {
        try {
            professeurService.deleteProfesseur(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Professeur supprimé");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
