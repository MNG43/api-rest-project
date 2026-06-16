package com.universite.consumer.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceEnrichissementDonnees {

    private static final String CONSUMER_SERVICE = "API REST Consumer";
    private static final String CONSUMER_VERSION = "1.0.0";

    public Map<String, Object> enrichirListe(String nomRessource, List<Map<String, Object>> elements) {
        List<Map<String, Object>> elementsEnrichis = new ArrayList<>();

        for (Map<String, Object> element : elements) {
            elementsEnrichis.add(enrichirElement(element));
        }

        Map<String, Object> resume = new HashMap<>();
        resume.put("resource", nomRessource);
        resume.put("total", elementsEnrichis.size());
        resume.put("items", elementsEnrichis);
        ajouterMetadonneesConsommateur(resume, "SUCCESS");
        return resume;
    }

    public Map<String, Object> enrichirElementUnique(String nomRessource, Map<String, Object> element) {
        Map<String, Object> resultat = enrichirElement(element);
        resultat.put("resource", nomRessource);
        ajouterMetadonneesConsommateur(resultat, "SUCCESS");
        return resultat;
    }

    public Map<String, Object> enrichirResumeUniversite(Map<String, Integer> comptesParRessource) {
        int totalEnregistrements = comptesParRessource.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Object> resume = new HashMap<>();
        resume.put("type", "resume_universite");
        resume.put("total_ressources", comptesParRessource.size());
        resume.put("total_enregistrements", totalEnregistrements);
        resume.put("comptes_par_ressource", comptesParRessource);
        resume.put("message", "Synthèse agrégée à partir des appels HTTP vers le service fournisseur");
        ajouterMetadonneesConsommateur(resume, "SUCCESS");
        return resume;
    }

    private Map<String, Object> enrichirElement(Map<String, Object> element) {
        Map<String, Object> enrichi = new HashMap<>(element);

        Object nom = element.get("nom");
        Object prenom = element.get("prenom");
        if (nom != null && prenom != null) {
            enrichi.put("nom_complet", prenom + " " + nom);
        }

        Object email = element.get("email");
        if (email != null) {
            String emailStr = String.valueOf(email);
            int atIndex = emailStr.indexOf('@');
            if (atIndex >= 0 && atIndex < emailStr.length() - 1) {
                enrichi.put("domaine_email", emailStr.substring(atIndex + 1));
            }
        }

        enrichi.put("traite_par", CONSUMER_SERVICE);
        return enrichi;
    }

    private void ajouterMetadonneesConsommateur(Map<String, Object> reponse, String statut) {
        reponse.put("consumer_timestamp", LocalDateTime.now().toString());
        reponse.put("consumer_service", CONSUMER_SERVICE);
        reponse.put("consumer_version", CONSUMER_VERSION);
        reponse.put("processing_status", statut);
        reponse.put("data_source", "HTTP call to provider service");
    }

    /**
     * Prépare les données avant création.
     * L'enrichissement consommateur reste dans la réponse HTTP afin de ne pas
     * envoyer de champs inconnus aux entités JPA du fournisseur.
     */
    public Map<String, Object> enrichirDonneesCreation(String nomRessource, Map<String, Object> donnees) {
        return new HashMap<>(donnees);
    }

    /**
     * Prépare les données avant modification sans ajouter de champs techniques
     * au payload transmis au fournisseur.
     */
    public Map<String, Object> enrichirDonneesModification(String nomRessource, Long id, Map<String, Object> donnees) {
        return new HashMap<>(donnees);
    }
}
