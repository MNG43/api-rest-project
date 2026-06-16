package com.universite.apirest.service;

import com.universite.apirest.entity.Professeur;
import com.universite.apirest.repository.ProfesseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;

    public ProfesseurService(ProfesseurRepository professeurRepository) {
        this.professeurRepository = professeurRepository;
    }

    public List<Professeur> getAllProfesseurs() {
        return professeurRepository.findAll();
    }

    public Optional<Professeur> getProfesseurById(Long id) {
        return professeurRepository.findById(id);
    }

    public Professeur createProfesseur(Professeur professeur) {
        if (professeurRepository.existsByEmail(professeur.getEmail())) {
            throw new IllegalArgumentException("Un professeur avec cet email existe déjà");
        }
        if (professeurRepository.existsByTelephone(professeur.getTelephone())) {
            throw new IllegalArgumentException("Un professeur avec ce téléphone existe déjà");
        }
        return professeurRepository.save(professeur);
    }

    public Professeur updateProfesseur(Long id, Professeur professeurDetails) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professeur non trouvé avec l'ID: " + id));

        professeur.setNom(professeurDetails.getNom());
        professeur.setPrenom(professeurDetails.getPrenom());
        professeur.setEmail(professeurDetails.getEmail());
        professeur.setTelephone(professeurDetails.getTelephone());
        professeur.setSpecialite(professeurDetails.getSpecialite());
        professeur.setGrade(professeurDetails.getGrade());

        return professeurRepository.save(professeur);
    }

    public void deleteProfesseur(Long id) {
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professeur non trouvé avec l'ID: " + id));
        professeurRepository.delete(professeur);
    }
}
