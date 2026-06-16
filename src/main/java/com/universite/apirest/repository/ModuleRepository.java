package com.universite.apirest.repository;

import com.universite.apirest.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findByCode(String code);
    boolean existsByCode(String code);
}
