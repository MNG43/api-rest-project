package com.universite.apirest.controller;

import com.universite.apirest.entity.Cours;
import com.universite.apirest.entity.Fillier;
import com.universite.apirest.entity.Niveau;
import com.universite.apirest.service.CoursService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cours")
public class CoursController {

    private final CoursService coursService;

    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    @GetMapping
    public ResponseEntity<List<Cours>> getAllCours() {
        List<Cours> cours = coursService.getAllCours();
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoursById(@PathVariable Long id) {
        Optional<Cours> cours = coursService.getCoursById(id);
        if (cours.isPresent()) {
            return ResponseEntity.ok(cours.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cours non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/filiere/{filiere}/niveau/{niveau}")
    public ResponseEntity<List<Cours>> getCoursByFiliereAndNiveau(
            @PathVariable Fillier filiere, 
            @PathVariable Niveau niveau) {
        List<Cours> cours = coursService.getCoursByFiliereAndNiveau(filiere, niveau);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/professeur/{professeurId}")
    public ResponseEntity<List<Cours>> getCoursByProfesseur(@PathVariable Long professeurId) {
        List<Cours> cours = coursService.getCoursByProfesseur(professeurId);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Cours>> getCoursByModule(@PathVariable Long moduleId) {
        List<Cours> cours = coursService.getCoursByModule(moduleId);
        return ResponseEntity.ok(cours);
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Cours>> getCoursBySemestre(@PathVariable Integer semestre) {
        List<Cours> cours = coursService.getCoursBySemestre(semestre);
        return ResponseEntity.ok(cours);
    }

    @PostMapping
    public ResponseEntity<?> createCours(@Valid @RequestBody Cours cours) {
        try {
            Cours createdCours = coursService.createCours(cours);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cours créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCours(@PathVariable Long id, @Valid @RequestBody Cours cours) {
        try {
            Cours updatedCours = coursService.updateCours(id, cours);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cours modifié");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCours(@PathVariable Long id) {
        try {
            coursService.deleteCours(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cours supprimé");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
