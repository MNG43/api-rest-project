package com.universite.apirest.service;

import com.universite.apirest.entity.Etudiant;
import com.universite.apirest.repository.EtudiantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Etudiant createEtudiant(Etudiant etudiant) {
        if (etudiantRepository.findByEmail(etudiant.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un étudiant avec cet email existe déjà");
        }
        return etudiantRepository.save(etudiant);
    }

    public Etudiant updateEtudiant(Long id, Etudiant etudiantDetails) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé avec l'ID: " + id));

        etudiant.setNom(etudiantDetails.getNom());
        etudiant.setPrenom(etudiantDetails.getPrenom());
        etudiant.setEmail(etudiantDetails.getEmail());

        return etudiantRepository.save(etudiant);
    }

    public void deleteEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé avec l'ID: " + id));
        etudiantRepository.delete(etudiant);
    }
}
