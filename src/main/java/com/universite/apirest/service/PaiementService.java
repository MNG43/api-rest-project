package com.universite.apirest.service;

import com.universite.apirest.entity.Etudiant;
import com.universite.apirest.entity.Paiement;
import com.universite.apirest.repository.EtudiantRepository;
import com.universite.apirest.repository.PaiementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final EtudiantRepository etudiantRepository;

    public PaiementService(PaiementRepository paiementRepository, EtudiantRepository etudiantRepository) {
        this.paiementRepository = paiementRepository;
        this.etudiantRepository = etudiantRepository;
    }

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public List<Paiement> getPaiementsByEtudiant(Long etudiantId) {
        return paiementRepository.findByEtudiantId(etudiantId);
    }

    public List<Paiement> getPaiementsByEtudiantAndAnnee(Long etudiantId, Integer anneeAcademique) {
        return paiementRepository.findByEtudiantIdAndAnneeAcademique(etudiantId, anneeAcademique);
    }

    public Paiement createPaiement(Paiement paiement) {
        Etudiant etudiant = etudiantRepository.findById(paiement.getEtudiant().getId())
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));

        paiement.setEtudiant(etudiant);
        paiement.setStatut("EN_ATTENTE");
        paiement.setReferenceTransaction(generateReferenceTransaction());

        return paiementRepository.save(paiement);
    }

    public Map<String, Object> effectuerPaiement(Long paiementId, String methodePaiement) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new IllegalArgumentException("Paiement non trouvé"));

        if (!"EN_ATTENTE".equals(paiement.getStatut())) {
            throw new IllegalArgumentException("Ce paiement a déjà été traité");
        }

        if (!methodePaiement.equals(paiement.getMethodePaiement())) {
            throw new IllegalArgumentException("Méthode de paiement invalide");
        }

        Map<String, Object> resultat = simulerPaiement(methodePaiement, paiement.getMontant());

        if ((Boolean) resultat.get("succes")) {
            paiement.setStatut("PAYE");
            paiement.setDatePaiement(LocalDateTime.now());
            paiement.setReferenceTransaction((String) resultat.get("reference"));
            paiementRepository.save(paiement);
        } else {
            paiement.setStatut("ECHOUE");
            paiementRepository.save(paiement);
        }

        return resultat;
    }

    private Map<String, Object> simulerPaiement(String methode, BigDecimal montant) {
        Map<String, Object> resultat = new HashMap<>();
        Random random = new Random();

        boolean succes = random.nextDouble() > 0.1; // 90% de succès

        if (succes) {
            resultat.put("succes", true);
            resultat.put("message", "Paiement effectué avec succès");
            resultat.put("reference", generateReferenceTransaction());
            resultat.put("montant", montant);
            resultat.put("methode", methode);
            resultat.put("date", LocalDateTime.now());
        } else {
            resultat.put("succes", false);
            resultat.put("message", "Échec du paiement - Veuillez réessayer");
            resultat.put("codeErreur", random.nextInt(900) + 100);
        }

        return resultat;
    }

    private String generateReferenceTransaction() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Map<String, Object> getSoldeEtudiant(Long etudiantId, Integer anneeAcademique) {
        List<Paiement> paiements = paiementRepository.findByEtudiantIdAndAnneeAcademique(etudiantId, anneeAcademique);
        
        BigDecimal totalPaye = paiements.stream()
                .filter(p -> "PAYE".equals(p.getStatut()))
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalEnAttente = paiements.stream()
                .filter(p -> "EN_ATTENTE".equals(p.getStatut()))
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> solde = new HashMap<>();
        solde.put("totalPaye", totalPaye);
        solde.put("totalEnAttente", totalEnAttente);
        solde.put("nombrePaiements", paiements.size());
        solde.put("anneeAcademique", anneeAcademique);

        return solde;
    }

    public void deletePaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paiement non trouvé avec l'ID: " + id));
        
        if ("PAYE".equals(paiement.getStatut())) {
            throw new IllegalArgumentException("Impossible de supprimer un paiement déjà effectué");
        }
        
        paiementRepository.delete(paiement);
    }
}
