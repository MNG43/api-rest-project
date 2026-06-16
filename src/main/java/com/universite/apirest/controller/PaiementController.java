package com.universite.apirest.controller;

import com.universite.apirest.entity.Paiement;
import com.universite.apirest.service.PaiementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/paiements")
public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        List<Paiement> paiements = paiementService.getAllPaiements();
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Paiement>> getPaiementsByEtudiant(@PathVariable Long etudiantId) {
        List<Paiement> paiements = paiementService.getPaiementsByEtudiant(etudiantId);
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/etudiant/{etudiantId}/annee/{anneeAcademique}")
    public ResponseEntity<List<Paiement>> getPaiementsByEtudiantAndAnnee(
            @PathVariable Long etudiantId, 
            @PathVariable Integer anneeAcademique) {
        List<Paiement> paiements = paiementService.getPaiementsByEtudiantAndAnnee(etudiantId, anneeAcademique);
        return ResponseEntity.ok(paiements);
    }

    @PostMapping
    public ResponseEntity<?> createPaiement(@Valid @RequestBody Paiement paiement) {
        try {
            Paiement createdPaiement = paiementService.createPaiement(paiement);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Paiement créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/{paiementId}/effectuer")
    public ResponseEntity<?> effectuerPaiement(
            @PathVariable Long paiementId,
            @RequestBody Map<String, String> request) {
        try {
            String methodePaiement = request.get("methodePaiement");
            Map<String, Object> resultat = paiementService.effectuerPaiement(paiementId, methodePaiement);
            return ResponseEntity.ok(resultat);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/etudiant/{etudiantId}/solde/annee/{anneeAcademique}")
    public ResponseEntity<?> getSoldeEtudiant(
            @PathVariable Long etudiantId, 
            @PathVariable Integer anneeAcademique) {
        Map<String, Object> solde = paiementService.getSoldeEtudiant(etudiantId, anneeAcademique);
        return ResponseEntity.ok(solde);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaiement(@PathVariable Long id) {
        try {
            paiementService.deletePaiement(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Paiement supprimé");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
