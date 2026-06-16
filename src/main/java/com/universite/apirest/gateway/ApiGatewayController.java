package com.universite.apirest.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiGatewayController {

    @GetMapping("/api")
    public Map<String, String> routeToService(@RequestParam(required = false) String service) {
        Map<String, String> response = new HashMap<>();

        if (service == null || service.isEmpty()) {
            response.put("message", "Veuillez spécifier un service");
            response.put("available_services", "auth, etudiants, professeurs, modules, cours, notes, paiements, salles, emploi-du-temps, notifications");
            return response;
        }

        switch (service.toLowerCase()) {
            case "auth":
                response.put("message", "Service authentification disponible");
                response.put("endpoint", "/api/v1/auth");
                response.put("description", "Login, logout, inscription, JWT");
                break;
            case "etudiants":
                response.put("message", "Service etudiants disponible");
                response.put("endpoint", "/api/v1/students");
                response.put("description", "CRUD étudiants, profil, historique");
                break;
            case "professeurs":
                response.put("message", "Service professeurs disponible");
                response.put("endpoint", "/api/v1/professeurs");
                response.put("description", "CRUD professeurs, spécialités");
                break;
            case "modules":
                response.put("message", "Service modules disponible");
                response.put("endpoint", "/api/v1/modules");
                response.put("description", "CRUD modules, crédits, volume horaire");
                break;
            case "cours":
                response.put("message", "Service cours disponible");
                response.put("endpoint", "/api/v1/cours");
                response.put("description", "CRUD cours, attribution professeurs, filières");
                break;
            case "notes":
                response.put("message", "Service notes disponible");
                response.put("endpoint", "/api/v1/notes");
                response.put("description", "Saisie notes, calcul moyenne, bulletins");
                break;
            case "paiements":
                response.put("message", "Service paiements disponible");
                response.put("endpoint", "/api/v1/paiements");
                response.put("description", "Frais inscription, scolarité, Wave/Orange Money");
                break;
            case "salles":
                response.put("message", "Service salles disponible");
                response.put("endpoint", "/api/v1/salles");
                response.put("description", "CRUD salles, capacités, équipements");
                break;
            case "emploi-du-temps":
                response.put("message", "Service emploi du temps disponible");
                response.put("endpoint", "/api/v1/emploi-du-temps");
                response.put("description", "Planning cours, salles, horaires");
                break;
            case "notifications":
                response.put("message", "Service notifications disponible");
                response.put("endpoint", "/api/v1/notifications");
                response.put("description", "Email/SMS, alertes notes/paiements");
                break;
            default:
                response.put("message", "Service introuvable");
                response.put("available_services", "auth, etudiants, professeurs, modules, cours, notes, paiements, salles, emploi-du-temps, notifications");
        }

        return response;
    }
}
