package com.universite.apirest.controller;

import com.universite.apirest.entity.Notification;
import com.universite.apirest.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Notification>> getNotificationsByEtudiant(@PathVariable Long etudiantId) {
        List<Notification> notifications = notificationService.getNotificationsByEtudiant(etudiantId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/etudiant/{etudiantId}/lu/{lu}")
    public ResponseEntity<List<Notification>> getNotificationsByEtudiantAndLu(
            @PathVariable Long etudiantId, 
            @PathVariable boolean lu) {
        List<Notification> notifications = notificationService.getNotificationsByEtudiantAndLu(etudiantId, lu);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Notification>> getNotificationsByStatut(@PathVariable String statut) {
        List<Notification> notifications = notificationService.getNotificationsByStatut(statut);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable String type) {
        List<Notification> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@Valid @RequestBody Notification notification) {
        try {
            Notification createdNotification = notificationService.createNotification(notification);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notification créée avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/{notificationId}/envoyer")
    public ResponseEntity<?> envoyerNotification(@PathVariable Long notificationId) {
        try {
            Notification notification = notificationService.envoyerNotification(notificationId);
            return ResponseEntity.ok(notification);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{notificationId}/lire")
    public ResponseEntity<?> marquerCommeLue(@PathVariable Long notificationId) {
        try {
            Notification notification = notificationService.marquerCommeLue(notificationId);
            return ResponseEntity.ok(notification);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/note")
    public ResponseEntity<?> creerNotificationNote(@RequestBody Map<String, String> request) {
        try {
            Long etudiantId = Long.parseLong(request.get("etudiantId"));
            String titre = request.get("titre");
            String message = request.get("message");
            Notification notification = notificationService.creerNotificationNote(etudiantId, titre, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/paiement")
    public ResponseEntity<?> creerNotificationPaiement(@RequestBody Map<String, String> request) {
        try {
            Long etudiantId = Long.parseLong(request.get("etudiantId"));
            String titre = request.get("titre");
            String message = request.get("message");
            Notification notification = notificationService.creerNotificationPaiement(etudiantId, titre, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/cours")
    public ResponseEntity<?> creerNotificationCours(@RequestBody Map<String, String> request) {
        try {
            Long etudiantId = Long.parseLong(request.get("etudiantId"));
            String titre = request.get("titre");
            String message = request.get("message");
            Notification notification = notificationService.creerNotificationCours(etudiantId, titre, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notification supprimée");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
