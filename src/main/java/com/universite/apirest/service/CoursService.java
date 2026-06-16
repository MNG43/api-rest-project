package com.universite.apirest.service;

import com.universite.apirest.entity.Cours;
import com.universite.apirest.entity.Fillier;
import com.universite.apirest.entity.Niveau;
import com.universite.apirest.repository.CoursRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoursService {

    private final CoursRepository coursRepository;

    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }

    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }

    public List<Cours> getCoursByFiliereAndNiveau(Fillier filiere, Niveau niveau) {
        return coursRepository.findByFiliereAndNiveau(filiere, niveau);
    }

    public List<Cours> getCoursByProfesseur(Long professeurId) {
        return coursRepository.findByProfesseurId(professeurId);
    }

    public List<Cours> getCoursByModule(Long moduleId) {
        return coursRepository.findByModuleId(moduleId);
    }

    public List<Cours> getCoursBySemestre(Integer semestre) {
        return coursRepository.findBySemestre(semestre);
    }

    public Cours createCours(Cours cours) {
        return coursRepository.save(cours);
    }

    public Cours updateCours(Long id, Cours coursDetails) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cours non trouvé avec l'ID: " + id));

        cours.setTitre(coursDetails.getTitre());
        cours.setDescription(coursDetails.getDescription());
        cours.setModule(coursDetails.getModule());
        cours.setProfesseur(coursDetails.getProfesseur());
        cours.setFiliere(coursDetails.getFiliere());
        cours.setNiveau(coursDetails.getNiveau());
        cours.setSemestre(coursDetails.getSemestre());

        return coursRepository.save(cours);
    }

    public void deleteCours(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cours non trouvé avec l'ID: " + id));
        coursRepository.delete(cours);
    }
}
