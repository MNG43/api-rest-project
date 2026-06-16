package com.universite.consumer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GestionnaireGlobalExceptions {

    @ExceptionHandler(com.universite.consumer.service.ExceptionServiceFournisseurIndisponible.class)
    public ResponseEntity<Map> handleProviderUnavailableException(
            com.universite.consumer.service.ExceptionServiceFournisseurIndisponible ex) {
        log.error("Service fournisseur indisponible: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "PROVIDER_SERVICE_UNAVAILABLE");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map> handleResourceAccessException(ResourceAccessException ex) {
        log.error("Erreur de connexion au service fournisseur: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "CONNECTION_ERROR");
        response.put("message", "Impossible de se connecter au service fournisseur");
        response.put("details", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map> handleRestClientException(RestClientException ex) {
        log.error("Erreur lors de l'appel REST: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "REST_CLIENT_ERROR");
        response.put("message", "Erreur lors de l'appel HTTP au service fournisseur");
        response.put("details", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.BAD_GATEWAY.value());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map> handleGenericException(Exception ex) {
        log.error("Erreur non gérée: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "INTERNAL_SERVER_ERROR");
        response.put("message", "Une erreur interne est survenue");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
