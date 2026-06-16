package com.universite.consumer.service;

import com.universite.consumer.config.CarteRessourcesFournisseur;
import com.universite.consumer.config.ConfigurationServiceFournisseur;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceClientFournisseur {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {};

    private final RestTemplate restTemplate;
    private final ConfigurationServiceFournisseur configurationServiceFournisseur;
    private final CarteRessourcesFournisseur carteRessourcesFournisseur;

    /**
     * Appelle le service fournisseur pour obtenir les informations d'un service
     */
    public ResponseEntity<Map<String, Object>> getServiceInfo(String serviceName) {
        try {
            String url = configurationServiceFournisseur.getBaseUrl() + "/api?service=" + serviceName;
            log.info("Appel du service fournisseur: {}", url);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    MAP_RESPONSE_TYPE
            );
            log.info("Réponse reçue du service fournisseur: {}", response.getStatusCode());

            return response;

        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors de l'appel au service fournisseur: {}", e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible("Service fournisseur indisponible: " + e.getMessage());
        }
    }

    /**
     * Récupère la liste d'une ressource métier exposée par le fournisseur
     */
    public List<Map<String, Object>> getResourceList(String resourceName) {
        String path = carteRessourcesFournisseur.getChemin(resourceName)
                .orElseThrow(() -> new IllegalArgumentException("Ressource inconnue: " + resourceName));

        try {
            String url = configurationServiceFournisseur.getBaseUrl() + path;
            log.info("Appel HTTP du fournisseur pour la ressource {}: {}", resourceName, url);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            return response.getBody() != null ? response.getBody() : List.of();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la ressource {}: {}", resourceName, e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible(
                    "Impossible de récupérer la ressource " + resourceName + ": " + e.getMessage()
            );
        }
    }

    /**
     * Récupère un élément précis d'une ressource métier
     */
    public Map<String, Object> getResourceById(String resourceName, Long id) {
        String path = carteRessourcesFournisseur.getChemin(resourceName)
                .orElseThrow(() -> new IllegalArgumentException("Ressource inconnue: " + resourceName));

        try {
            String url = configurationServiceFournisseur.getBaseUrl() + path + "/" + id;
            log.info("Appel HTTP du fournisseur pour {} #{}: {}", resourceName, id, url);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    MAP_RESPONSE_TYPE
            );
            return response.getBody() != null ? response.getBody() : Map.of();
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Aucun élément trouvé pour " + resourceName + " #" + id);
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de {} #{}: {}", resourceName, id, e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible(
                    "Impossible de récupérer " + resourceName + " #" + id + ": " + e.getMessage()
            );
        }
    }

    /**
     * Vérifie si le service fournisseur est disponible
     */
    public boolean isProviderAvailable() {
        try {
            String url = configurationServiceFournisseur.getBaseUrl() + "/api";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("Service fournisseur non disponible: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Crée une nouvelle ressource via le service fournisseur
     */
    public ResponseEntity<Map<String, Object>> createResource(String resourceName, Map<String, Object> resourceData) {
        String path = carteRessourcesFournisseur.getChemin(resourceName)
                .orElseThrow(() -> new IllegalArgumentException("Ressource inconnue: " + resourceName));

        try {
            String url = configurationServiceFournisseur.getBaseUrl() + path;
            log.info("Création de la ressource {} via le fournisseur: {}", resourceName, url);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(resourceData),
                    MAP_RESPONSE_TYPE
            );
            log.info("Ressource {} créée avec succès", resourceName);
            return response;
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors de la création de la ressource {}: {}", resourceName, e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible(
                    "Impossible de créer la ressource " + resourceName + ": " + e.getMessage()
            );
        }
    }

    /**
     * Met à jour une ressource existante via le service fournisseur
     */
    public ResponseEntity<Map<String, Object>> updateResource(String resourceName, Long id, Map<String, Object> resourceData) {
        String path = carteRessourcesFournisseur.getChemin(resourceName)
                .orElseThrow(() -> new IllegalArgumentException("Ressource inconnue: " + resourceName));

        try {
            String url = configurationServiceFournisseur.getBaseUrl() + path + "/" + id;
            log.info("Mise à jour de la ressource {} #{} via le fournisseur: {}", resourceName, id, url);

            restTemplate.put(url, resourceData);
            
            // Récupérer la ressource mise à jour
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    MAP_RESPONSE_TYPE
            );
            log.info("Ressource {} #{} mise à jour avec succès", resourceName, id);
            return response;
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de {} #{}: {}", resourceName, id, e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible(
                    "Impossible de mettre à jour " + resourceName + " #" + id + ": " + e.getMessage()
            );
        }
    }

    /**
     * Supprime une ressource via le service fournisseur
     */
    public ResponseEntity<Map<String, Object>> deleteResource(String resourceName, Long id) {
        String path = carteRessourcesFournisseur.getChemin(resourceName)
                .orElseThrow(() -> new IllegalArgumentException("Ressource inconnue: " + resourceName));

        try {
            String url = configurationServiceFournisseur.getBaseUrl() + path + "/" + id;
            log.info("Suppression de la ressource {} #{} via le fournisseur: {}", resourceName, id, url);

            restTemplate.delete(url);
            
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("id", id);
            response.put("status", "deleted");
            
            log.info("Ressource {} #{} supprimée avec succès", resourceName, id);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de {} #{}: {}", resourceName, id, e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible(
                    "Impossible de supprimer " + resourceName + " #" + id + ": " + e.getMessage()
            );
        }
    }

    /**
     * Demande au fournisseur de charger ses données de démonstration.
     */
    public ResponseEntity<Map<String, Object>> loadTestData() {
        try {
            String url = configurationServiceFournisseur.getBaseUrl() + "/api/v1/test/load-data";
            log.info("Chargement des données de test via le fournisseur: {}", url);
            return restTemplate.exchange(url, HttpMethod.POST, null, MAP_RESPONSE_TYPE);
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException(extraireMessageFournisseur(e));
        } catch (Exception e) {
            log.error("Erreur lors du chargement des données de test: {}", e.getMessage());
            throw new ExceptionServiceFournisseurIndisponible(
                    "Impossible de charger les données de test: " + e.getMessage()
            );
        }
    }

    private String extraireMessageFournisseur(RestClientResponseException exception) {
        String responseBody = exception.getResponseBodyAsString();
        if (responseBody != null && !responseBody.isBlank()) {
            return responseBody;
        }
        return "Erreur fournisseur HTTP " + exception.getStatusCode().value();
    }
}
