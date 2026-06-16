package com.universite.apirest.controller;

import com.universite.apirest.entity.EmploiDuTemps;
import com.universite.apirest.service.EmploiDuTempsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/emploi-du-temps")
public class EmploiDuTempsController {

    private final EmploiDuTempsService emploiDuTempsService;

    public EmploiDuTempsController(EmploiDuTempsService emploiDuTempsService) {
        this.emploiDuTempsService = emploiDuTempsService;
    }

    @GetMapping
    public ResponseEntity<List<EmploiDuTemps>> getAllEmploiDuTemps() {
        List<EmploiDuTemps> emploiDuTemps = emploiDuTempsService.getAllEmploiDuTemps();
        return ResponseEntity.ok(emploiDuTemps);
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<EmploiDuTemps>> getEmploiDuTempsByCours(@PathVariable Long coursId) {
        List<EmploiDuTemps> emploiDuTemps = emploiDuTempsService.getEmploiDuTempsByCours(coursId);
        return ResponseEntity.ok(emploiDuTemps);
    }

    @GetMapping("/salle/{salleId}")
    public ResponseEntity<List<EmploiDuTemps>> getEmploiDuTempsBySalle(@PathVariable Long salleId) {
        List<EmploiDuTemps> emploiDuTemps = emploiDuTempsService.getEmploiDuTempsBySalle(salleId);
        return ResponseEntity.ok(emploiDuTemps);
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<EmploiDuTemps>> getEmploiDuTempsBySemestre(@PathVariable Integer semestre) {
        List<EmploiDuTemps> emploiDuTemps = emploiDuTempsService.getEmploiDuTempsBySemestre(semestre);
        return ResponseEntity.ok(emploiDuTemps);
    }

    @GetMapping("/semestre/{semestre}/annee/{anneeAcademique}")
    public ResponseEntity<List<EmploiDuTemps>> getEmploiDuTempsBySemestreAndAnnee(
            @PathVariable Integer semestre, 
            @PathVariable Integer anneeAcademique) {
        List<EmploiDuTemps> emploiDuTemps = emploiDuTempsService.getEmploiDuTempsBySemestreAndAnnee(semestre, anneeAcademique);
        return ResponseEntity.ok(emploiDuTemps);
    }

    @PostMapping
    public ResponseEntity<?> createEmploiDuTemps(@Valid @RequestBody EmploiDuTemps emploiDuTemps) {
        try {
            EmploiDuTemps createdEmploiDuTemps = emploiDuTempsService.createEmploiDuTemps(emploiDuTemps);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Emploi du temps créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmploiDuTemps(@PathVariable Long id, @Valid @RequestBody EmploiDuTemps emploiDuTemps) {
        try {
            EmploiDuTemps updatedEmploiDuTemps = emploiDuTempsService.updateEmploiDuTemps(id, emploiDuTemps);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Emploi du temps modifié");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmploiDuTemps(@PathVariable Long id) {
        try {
            emploiDuTempsService.deleteEmploiDuTemps(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Emploi du temps supprimé");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/salle/{salleId}/disponibilite")
    public ResponseEntity<?> verifierDisponibiliteSalle(
            @PathVariable Long salleId,
            @RequestParam DayOfWeek jour,
            @RequestParam LocalTime heureDebut,
            @RequestParam LocalTime heureFin) {
        boolean disponible = emploiDuTempsService.verifierDisponibiliteSalle(salleId, jour, heureDebut, heureFin);
        Map<String, Object> response = new HashMap<>();
        response.put("disponible", disponible);
        return ResponseEntity.ok(response);
    }
}
