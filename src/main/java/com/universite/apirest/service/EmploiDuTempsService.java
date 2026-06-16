package com.universite.apirest.service;

import com.universite.apirest.entity.Cours;
import com.universite.apirest.entity.EmploiDuTemps;
import com.universite.apirest.entity.Salle;
import com.universite.apirest.repository.CoursRepository;
import com.universite.apirest.repository.EmploiDuTempsRepository;
import com.universite.apirest.repository.SalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class EmploiDuTempsService {

    private final EmploiDuTempsRepository emploiDuTempsRepository;
    private final CoursRepository coursRepository;
    private final SalleRepository salleRepository;

    public EmploiDuTempsService(EmploiDuTempsRepository emploiDuTempsRepository,
                               CoursRepository coursRepository,
                               SalleRepository salleRepository) {
        this.emploiDuTempsRepository = emploiDuTempsRepository;
        this.coursRepository = coursRepository;
        this.salleRepository = salleRepository;
    }

    public List<EmploiDuTemps> getAllEmploiDuTemps() {
        return emploiDuTempsRepository.findAll();
    }

    public List<EmploiDuTemps> getEmploiDuTempsByCours(Long coursId) {
        return emploiDuTempsRepository.findByCoursId(coursId);
    }

    public List<EmploiDuTemps> getEmploiDuTempsBySalle(Long salleId) {
        return emploiDuTempsRepository.findBySalleId(salleId);
    }

    public List<EmploiDuTemps> getEmploiDuTempsBySemestre(Integer semestre) {
        return emploiDuTempsRepository.findBySemestre(semestre);
    }

    public List<EmploiDuTemps> getEmploiDuTempsBySemestreAndAnnee(Integer semestre, Integer anneeAcademique) {
        return emploiDuTempsRepository.findBySemestreAndAnneeAcademique(semestre, anneeAcademique);
    }

    public EmploiDuTemps createEmploiDuTemps(EmploiDuTemps emploiDuTemps) {
        Cours cours = coursRepository.findById(emploiDuTemps.getCours().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cours non trouvé"));

        Salle salle = salleRepository.findById(emploiDuTemps.getSalle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Salle non trouvée"));

        if (emploiDuTemps.getHeureDebut().isAfter(emploiDuTemps.getHeureFin())) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }

        emploiDuTemps.setCours(cours);
        emploiDuTemps.setSalle(salle);

        return emploiDuTempsRepository.save(emploiDuTemps);
    }

    public EmploiDuTemps updateEmploiDuTemps(Long id, EmploiDuTemps emploiDuTempsDetails) {
        EmploiDuTemps emploiDuTemps = emploiDuTempsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emploi du temps non trouvé avec l'ID: " + id));

        if (emploiDuTempsDetails.getHeureDebut().isAfter(emploiDuTempsDetails.getHeureFin())) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }

        emploiDuTemps.setCours(emploiDuTempsDetails.getCours());
        emploiDuTemps.setSalle(emploiDuTempsDetails.getSalle());
        emploiDuTemps.setJour(emploiDuTempsDetails.getJour());
        emploiDuTemps.setHeureDebut(emploiDuTempsDetails.getHeureDebut());
        emploiDuTemps.setHeureFin(emploiDuTempsDetails.getHeureFin());
        emploiDuTemps.setSemestre(emploiDuTempsDetails.getSemestre());
        emploiDuTemps.setAnneeAcademique(emploiDuTempsDetails.getAnneeAcademique());

        return emploiDuTempsRepository.save(emploiDuTemps);
    }

    public void deleteEmploiDuTemps(Long id) {
        EmploiDuTemps emploiDuTemps = emploiDuTempsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emploi du temps non trouvé avec l'ID: " + id));
        emploiDuTempsRepository.delete(emploiDuTemps);
    }

    public boolean verifierDisponibiliteSalle(Long salleId, DayOfWeek jour, LocalTime heureDebut, LocalTime heureFin) {
        List<EmploiDuTemps> creneaux = emploiDuTempsRepository.findBySalleId(salleId);
        
        for (EmploiDuTemps creneau : creneaux) {
            if (creneau.getJour().equals(jour)) {
                if (heureDebut.isBefore(creneau.getHeureFin()) && heureFin.isAfter(creneau.getHeureDebut())) {
                    return false;
                }
            }
        }
        return true;
    }
}
