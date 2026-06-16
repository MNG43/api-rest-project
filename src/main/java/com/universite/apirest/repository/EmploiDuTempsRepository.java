package com.universite.apirest.repository;

import com.universite.apirest.entity.EmploiDuTemps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploiDuTempsRepository extends JpaRepository<EmploiDuTemps, Long> {
    List<EmploiDuTemps> findByCoursId(Long coursId);
    List<EmploiDuTemps> findBySalleId(Long salleId);
    List<EmploiDuTemps> findBySemestreAndAnneeAcademique(Integer semestre, Integer anneeAcademique);
    List<EmploiDuTemps> findBySemestre(Integer semestre);
}
