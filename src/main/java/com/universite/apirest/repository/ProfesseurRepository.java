package com.universite.apirest.repository;

import com.universite.apirest.entity.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    Optional<Professeur> findByEmail(String email);
    Optional<Professeur> findByTelephone(String telephone);
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
}
