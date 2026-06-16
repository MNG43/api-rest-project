package com.universite.apirest.controller;

import com.universite.apirest.entity.Etudiant;
import com.universite.apirest.service.EtudiantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
public class EtudiantController {

    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEtudiantById(@PathVariable Long id) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
        if (etudiant.isPresent()) {
            return ResponseEntity.ok(etudiant.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Étudiant non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEtudiant(@Valid @RequestBody Etudiant etudiant) {
        try {
            Etudiant createdEtudiant = etudiantService.createEtudiant(etudiant);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Étudiant créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEtudiant(@PathVariable Long id, @Valid @RequestBody Etudiant etudiant) {
        try {
            Etudiant updatedEtudiant = etudiantService.updateEtudiant(id, etudiant);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Étudiant modifié");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEtudiant(@PathVariable Long id) {
        try {
            etudiantService.deleteEtudiant(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Étudiant supprimé");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
