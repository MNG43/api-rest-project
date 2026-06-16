package com.universite.apirest.repository;

import com.universite.apirest.entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    Optional<Salle> findByCode(String code);
    boolean existsByCode(String code);
    List<Salle> findByBatiment(String batiment);
    List<Salle> findByType(String type);
    List<Salle> findByCapaciteGreaterThanEqual(Integer capacite);
}
