package com.universite.consumer.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class CarteRessourcesFournisseur {

    private static final Map<String, String> CHEMINS = Map.ofEntries(
            Map.entry("etudiants", "/api/v1/students"),
            Map.entry("professeurs", "/api/v1/professeurs"),
            Map.entry("modules", "/api/v1/modules"),
            Map.entry("cours", "/api/v1/cours"),
            Map.entry("notes", "/api/v1/notes"),
            Map.entry("paiements", "/api/v1/paiements"),
            Map.entry("salles", "/api/v1/salles"),
            Map.entry("emploi-du-temps", "/api/v1/emploi-du-temps"),
            Map.entry("notifications", "/api/v1/notifications")
    );

    public Optional<String> getChemin(String nomRessource) {
        if (nomRessource == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(CHEMINS.get(nomRessource.toLowerCase()));
    }

    public Set<String> getRessourcesDisponibles() {
        return CHEMINS.keySet();
    }
}
