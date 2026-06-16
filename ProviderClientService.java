package com.universite.consumer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

@Service
public class ProviderClientService {

    private final RestTemplate restTemplate;
    
    @Value("${provider.api.url:http://localhost:8082/api/v1}")
    private String providerUrl;

    public ProviderClientService() {
        this.restTemplate = new RestTemplate();
    }

    public Object fetchData(String resource) {
        try {
            String url = providerUrl + "/" + resource;
            Object data = restTemplate.getForObject(url, Object.class);
            return enrichData(data, resource, "SUCCESS");
        } catch (Exception e) {
            return createFallbackResponse(resource, e.getMessage());
        }
    }

    private Map<String, Object> enrichData(Object data, String resource, String status) {
        Map<String, Object> enriched = new HashMap<>();
        enriched.put("content", data);
        enriched.put("metadata", Map.of(
            "timestamp", LocalDateTime.now(),
            "source", "Provider-Service-8082",
            "resource", resource,
            "status", status,
            "version", "2.0.0"
        ));
        return enriched;
    }

    private Object createFallbackResponse(String resource, String error) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("content", null);
        fallback.put("error", "Service fournisseur indisponible: " + error);
        return enrichData(fallback, resource, "FALLBACK");
    }
}