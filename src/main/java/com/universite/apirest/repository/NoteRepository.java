package com.universite.apirest.repository;

import com.universite.apirest.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByEtudiantId(Long etudiantId);
    List<Note> findByCoursId(Long coursId);
    List<Note> findByEtudiantIdAndSemestre(Long etudiantId, Integer semestre);
    Optional<Note> findByEtudiantIdAndCoursId(Long etudiantId, Long coursId);
}
