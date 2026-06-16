package com.universite.apirest.repository;

import com.universite.apirest.entity.Cours;
import com.universite.apirest.entity.Fillier;
import com.universite.apirest.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    List<Cours> findByFiliereAndNiveau(Fillier filiere, Niveau niveau);
    List<Cours> findByProfesseurId(Long professeurId);
    List<Cours> findByModuleId(Long moduleId);
    List<Cours> findBySemestre(Integer semestre);
}
