package com.universite.apirest.service;

import com.universite.apirest.entity.Salle;
import com.universite.apirest.repository.SalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    public Optional<Salle> getSalleById(Long id) {
        return salleRepository.findById(id);
    }

    public Optional<Salle> getSalleByCode(String code) {
        return salleRepository.findByCode(code);
    }

    public List<Salle> getSallesByBatiment(String batiment) {
        return salleRepository.findByBatiment(batiment);
    }

    public List<Salle> getSallesByType(String type) {
        return salleRepository.findByType(type);
    }

    public List<Salle> getSallesByCapaciteMin(Integer capacite) {
        return salleRepository.findByCapaciteGreaterThanEqual(capacite);
    }

    public Salle createSalle(Salle salle) {
        if (salleRepository.existsByCode(salle.getCode())) {
            throw new IllegalArgumentException("Une salle avec ce code existe déjà");
        }
        return salleRepository.save(salle);
    }

    public Salle updateSalle(Long id, Salle salleDetails) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Salle non trouvée avec l'ID: " + id));

        salle.setCode(salleDetails.getCode());
        salle.setNom(salleDetails.getNom());
        salle.setBatiment(salleDetails.getBatiment());
        salle.setCapacite(salleDetails.getCapacite());
        salle.setType(salleDetails.getType());
        salle.setEquipementVideo(salleDetails.isEquipementVideo());

        return salleRepository.save(salle);
    }

    public void deleteSalle(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Salle non trouvée avec l'ID: " + id));
        salleRepository.delete(salle);
    }
}
