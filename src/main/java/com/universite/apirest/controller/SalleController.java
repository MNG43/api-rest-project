package com.universite.apirest.controller;

import com.universite.apirest.entity.Salle;
import com.universite.apirest.service.SalleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/salles")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    @GetMapping
    public ResponseEntity<List<Salle>> getAllSalles() {
        List<Salle> salles = salleService.getAllSalles();
        return ResponseEntity.ok(salles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSalleById(@PathVariable Long id) {
        Optional<Salle> salle = salleService.getSalleById(id);
        if (salle.isPresent()) {
            return ResponseEntity.ok(salle.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Salle non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getSalleByCode(@PathVariable String code) {
        Optional<Salle> salle = salleService.getSalleByCode(code);
        if (salle.isPresent()) {
            return ResponseEntity.ok(salle.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Salle non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/batiment/{batiment}")
    public ResponseEntity<List<Salle>> getSallesByBatiment(@PathVariable String batiment) {
        List<Salle> salles = salleService.getSallesByBatiment(batiment);
        return ResponseEntity.ok(salles);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Salle>> getSallesByType(@PathVariable String type) {
        List<Salle> salles = salleService.getSallesByType(type);
        return ResponseEntity.ok(salles);
    }

    @GetMapping("/capacite/{capacite}")
    public ResponseEntity<List<Salle>> getSallesByCapaciteMin(@PathVariable Integer capacite) {
        List<Salle> salles = salleService.getSallesByCapaciteMin(capacite);
        return ResponseEntity.ok(salles);
    }

    @PostMapping
    public ResponseEntity<?> createSalle(@Valid @RequestBody Salle salle) {
        try {
            Salle createdSalle = salleService.createSalle(salle);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Salle créée avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSalle(@PathVariable Long id, @Valid @RequestBody Salle salle) {
        try {
            Salle updatedSalle = salleService.updateSalle(id, salle);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Salle modifiée");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSalle(@PathVariable Long id) {
        try {
            salleService.deleteSalle(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Salle supprimée");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
