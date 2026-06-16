package com.universite.consumer.controller;

import com.universite.consumer.service.ServiceClientFournisseur;
import com.universite.consumer.service.ServiceEnrichissementDonnees;
import com.universite.consumer.service.ExceptionServiceFournisseurIndisponible;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/consumer")
@RequiredArgsConstructor
@Slf4j
public class ControleurConsommateur {

    private final ServiceClientFournisseur serviceClientFournisseur;
    private final ServiceEnrichissementDonnees serviceEnrichissementDonnees;

    /**
     * Endpoint enrichi qui appelle le service fournisseur et ajoute des métadonnées
     */
    @GetMapping("/service/{serviceName}")
    public ResponseEntity<Map> getEnrichedServiceInfo(@PathVariable String serviceName) {
        try {
            log.info("Demande d'informations enrichies pour le service: {}", serviceName);

            // Appel au service fournisseur
            ResponseEntity<Map<String, Object>> providerResponse = serviceClientFournisseur.getServiceInfo(serviceName);

            // Enrichissement des données
            Map<String, Object> enrichedResponse = copierCorpsReponse(providerResponse);
            enrichedResponse.put("consumer_timestamp", LocalDateTime.now().toString());
            enrichedResponse.put("consumer_service", "API REST Consumer");
            enrichedResponse.put("consumer_version", "1.0.0");
            enrichedResponse.put("processing_status", "SUCCESS");
            enrichedResponse.put("data_source", "HTTP call to provider service");

            log.info("Réponse enrichie générée avec succès");
            return ResponseEntity.ok(enrichedResponse);

        } catch (ExceptionServiceFournisseurIndisponible e) {
            log.error("Service fournisseur indisponible: {}", e.getMessage());

            // Réponse de dégradation gracieuse
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("error", "SERVICE_UNAVAILABLE");
            fallbackResponse.put("message", "Le service fournisseur est temporairement indisponible");
            fallbackResponse.put("requested_service", serviceName);
            fallbackResponse.put("consumer_timestamp", LocalDateTime.now().toString());
            fallbackResponse.put("consumer_service", "API REST Consumer");
            fallbackResponse.put("processing_status", "FALLBACK");
            fallbackResponse.put("suggestion", "Veuillez réessayer ultérieurement");

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
        }
    }

    /**
     * Endpoint de santé du service consommateur
     */
    @GetMapping("/health")
    public ResponseEntity<Map> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "API REST Consumer");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("port", "8083");

        // Vérification de la disponibilité du fournisseur
        boolean providerAvailable = serviceClientFournisseur.isProviderAvailable();
        health.put("provider_available", providerAvailable);
        health.put("provider_url", "http://localhost:8082");

        return ResponseEntity.ok(health);
    }

    /**
     * Endpoint qui liste tous les services disponibles via le fournisseur
     */
    @GetMapping("/services")
    public ResponseEntity<Map> listAllServices() {
        try {
            Map<String, Object> response = new HashMap<>();

            // Liste des services connus
            String[] services = {"auth", "etudiants", "professeurs", "modules", "cours", 
                                "notes", "paiements", "salles", "emploi-du-temps", "notifications"};
            
            response.put("available_services", services);
            response.put("total_services", services.length);
            response.put("consumer_timestamp", LocalDateTime.now().toString());
            response.put("usage_example", "GET /api/v1/consumer/service/{service_name} ou GET /api/v1/consumer/data/{resource_name}");

            // Vérification de la disponibilité du fournisseur
            boolean providerAvailable = serviceClientFournisseur.isProviderAvailable();
            response.put("provider_status", providerAvailable ? "AVAILABLE" : "UNAVAILABLE");

            if (!providerAvailable) {
                response.put("warning", "Le service fournisseur est actuellement indisponible");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erreur lors de la liste des services: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "INTERNAL_ERROR");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Appelle le fournisseur via HTTP, enrichit les données métier et expose le résultat
     */
    @GetMapping("/data/{resourceName}")
    public ResponseEntity<Map> getEnrichedResourceData(@PathVariable String resourceName) {
        try {
            log.info("Demande de données enrichies pour la ressource: {}", resourceName);

            List<Map<String, Object>> providerData = serviceClientFournisseur.getResourceList(resourceName);
            Map<String, Object> enrichedResponse = serviceEnrichissementDonnees.enrichirListe(resourceName, providerData);

            return ResponseEntity.ok(enrichedResponse);
        } catch (IllegalArgumentException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (ExceptionServiceFournisseurIndisponible e) {
            return buildFallbackResponse(resourceName, e.getMessage());
        }
    }

    /**
     * Appelle le fournisseur pour un élément précis et renvoie une version enrichie
     */
    @GetMapping("/data/{resourceName}/{id}")
    public ResponseEntity<Map> getEnrichedResourceItem(
            @PathVariable String resourceName,
            @PathVariable Long id) {
        try {
            log.info("Demande de données enrichies pour {} #{}", resourceName, id);

            Map<String, Object> providerData = serviceClientFournisseur.getResourceById(resourceName, id);
            Map<String, Object> enrichedResponse = serviceEnrichissementDonnees.enrichirElementUnique(resourceName, providerData);

            return ResponseEntity.ok(enrichedResponse);
        } catch (IllegalArgumentException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (ExceptionServiceFournisseurIndisponible e) {
            return buildFallbackResponse(resourceName, e.getMessage());
        }
    }

    /**
     * Agrège plusieurs appels HTTP au fournisseur et renvoie une synthèse enrichie
     */
    @GetMapping("/resume")
    public ResponseEntity<Map> getUniversitySummary() {
        try {
            String[] resources = {"etudiants", "professeurs", "modules", "cours", "notes",
                    "paiements", "salles", "emploi-du-temps", "notifications"};

            Map<String, Integer> counts = new LinkedHashMap<>();
            for (String resource : resources) {
                List<Map<String, Object>> data = serviceClientFournisseur.getResourceList(resource);
                counts.put(resource, data.size());
            }

            Map<String, Object> summary = serviceEnrichissementDonnees.enrichirResumeUniversite(counts);
            return ResponseEntity.ok(summary);
        } catch (ExceptionServiceFournisseurIndisponible e) {
            Map<String, Object> fallbackResponse = new HashMap<>();
            fallbackResponse.put("error", "SERVICE_UNAVAILABLE");
            fallbackResponse.put("message", "Le service fournisseur est temporairement indisponible");
            fallbackResponse.put("consumer_timestamp", LocalDateTime.now().toString());
            fallbackResponse.put("consumer_service", "API REST Consumer");
            fallbackResponse.put("processing_status", "FALLBACK");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
        }
    }

    private ResponseEntity<Map> buildBadRequestResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "BAD_REQUEST");
        errorResponse.put("message", message);
        errorResponse.put("consumer_timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ResponseEntity<Map> buildFallbackResponse(String resourceName, String details) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("error", "SERVICE_UNAVAILABLE");
        fallbackResponse.put("message", "Le service fournisseur est temporairement indisponible");
        fallbackResponse.put("requested_resource", resourceName);
        fallbackResponse.put("details", details);
        fallbackResponse.put("consumer_timestamp", LocalDateTime.now().toString());
        fallbackResponse.put("consumer_service", "API REST Consumer");
        fallbackResponse.put("processing_status", "FALLBACK");
        fallbackResponse.put("suggestion", "Veuillez réessayer ultérieurement");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
    }

    /**
     * Endpoint POST pour créer une nouvelle ressource
     */
    @PostMapping("/data/{resourceName}")
    public ResponseEntity<Map> createResource(
            @PathVariable String resourceName,
            @RequestBody Map<String, Object> resourceData) {
        try {
            log.info("Création d'une nouvelle ressource: {}", resourceName);

            // Enrichissement des données avant envoi au fournisseur
            Map<String, Object> enrichedData = serviceEnrichissementDonnees.enrichirDonneesCreation(resourceName, resourceData);
            
            // Appel au fournisseur pour créer la ressource
            ResponseEntity<Map<String, Object>> providerResponse = serviceClientFournisseur.createResource(resourceName, enrichedData);
            
            // Enrichissement de la réponse
            Map<String, Object> response = copierCorpsReponse(providerResponse);
            response.put("consumer_timestamp", LocalDateTime.now().toString());
            response.put("consumer_service", "API REST Consumer");
            response.put("processing_status", "CREATED");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (ExceptionServiceFournisseurIndisponible e) {
            return buildFallbackResponse(resourceName, e.getMessage());
        }
    }

    /**
     * Endpoint PUT pour mettre à jour une ressource existante
     */
    @PutMapping("/data/{resourceName}/{id}")
    public ResponseEntity<Map> updateResource(
            @PathVariable String resourceName,
            @PathVariable Long id,
            @RequestBody Map<String, Object> resourceData) {
        try {
            log.info("Mise à jour de la ressource {} #{}", resourceName, id);

            // Enrichissement des données avant envoi au fournisseur
            Map<String, Object> enrichedData = serviceEnrichissementDonnees.enrichirDonneesModification(resourceName, id, resourceData);
            
            // Appel au fournisseur pour mettre à jour la ressource
            ResponseEntity<Map<String, Object>> providerResponse = serviceClientFournisseur.updateResource(resourceName, id, enrichedData);
            
            // Enrichissement de la réponse
            Map<String, Object> response = copierCorpsReponse(providerResponse);
            response.put("consumer_timestamp", LocalDateTime.now().toString());
            response.put("consumer_service", "API REST Consumer");
            response.put("processing_status", "UPDATED");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (ExceptionServiceFournisseurIndisponible e) {
            return buildFallbackResponse(resourceName, e.getMessage());
        }
    }

    /**
     * Endpoint DELETE pour supprimer une ressource
     */
    @DeleteMapping("/data/{resourceName}/{id}")
    public ResponseEntity<Map> deleteResource(
            @PathVariable String resourceName,
            @PathVariable Long id) {
        try {
            log.info("Suppression de la ressource {} #{}", resourceName, id);

            // Appel au fournisseur pour supprimer la ressource
            ResponseEntity<Map<String, Object>> providerResponse = serviceClientFournisseur.deleteResource(resourceName, id);
            
            // Enrichissement de la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ressource supprimée avec succès");
            response.put("resource", resourceName);
            response.put("id", id);
            response.put("consumer_timestamp", LocalDateTime.now().toString());
            response.put("consumer_service", "API REST Consumer");
            response.put("processing_status", "DELETED");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (ExceptionServiceFournisseurIndisponible e) {
            return buildFallbackResponse(resourceName, e.getMessage());
        }
    }

    @PostMapping("/test-data")
    public ResponseEntity<Map> loadProviderTestData() {
        try {
            ResponseEntity<Map<String, Object>> providerResponse = serviceClientFournisseur.loadTestData();
            Map<String, Object> response = copierCorpsReponse(providerResponse);
            response.put("consumer_timestamp", LocalDateTime.now().toString());
            response.put("consumer_service", "API REST Consumer");
            response.put("processing_status", "TEST_DATA_LOADED");
            response.put("data_source", "HTTP call to provider service");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (ExceptionServiceFournisseurIndisponible e) {
            return buildFallbackResponse("test-data", e.getMessage());
        }
    }

    private Map<String, Object> copierCorpsReponse(ResponseEntity<Map<String, Object>> providerResponse) {
        Map<?, ?> body = providerResponse.getBody();
        Map<String, Object> copie = new HashMap<>();

        if (body != null) {
            body.forEach((cle, valeur) -> copie.put(String.valueOf(cle), valeur));
        }

        return copie;
    }
}
