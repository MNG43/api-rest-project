package com.universite.apirest.repository;

import com.universite.apirest.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByEtudiantId(Long etudiantId);
    List<Paiement> findByEtudiantIdAndAnneeAcademique(Long etudiantId, Integer anneeAcademique);
    List<Paiement> findByStatut(String statut);
}
