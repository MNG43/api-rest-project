package com.universite.apirest.service;

import com.universite.apirest.entity.Cours;
import com.universite.apirest.entity.Etudiant;
import com.universite.apirest.entity.Note;
import com.universite.apirest.repository.CoursRepository;
import com.universite.apirest.repository.EtudiantRepository;
import com.universite.apirest.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;

    public NoteService(NoteRepository noteRepository, EtudiantRepository etudiantRepository, CoursRepository coursRepository) {
        this.noteRepository = noteRepository;
        this.etudiantRepository = etudiantRepository;
        this.coursRepository = coursRepository;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesByEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }

    public List<Note> getNotesByEtudiantAndSemestre(Long etudiantId, Integer semestre) {
        return noteRepository.findByEtudiantIdAndSemestre(etudiantId, semestre);
    }

    public List<Note> getNotesByCours(Long coursId) {
        return noteRepository.findByCoursId(coursId);
    }

    public Optional<Note> getNoteByEtudiantAndCours(Long etudiantId, Long coursId) {
        return noteRepository.findByEtudiantIdAndCoursId(etudiantId, coursId);
    }

    public Note createNote(Note note) {
        Etudiant etudiant = etudiantRepository.findById(note.getEtudiant().getId())
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));
        
        Cours cours = coursRepository.findById(note.getCours().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cours non trouvé"));

        note.setEtudiant(etudiant);
        note.setCours(cours);

        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note noteDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note non trouvée avec l'ID: " + id));

        note.setValeur(noteDetails.getValeur());
        note.setTypeNote(noteDetails.getTypeNote());
        note.setSemestre(noteDetails.getSemestre());

        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note non trouvée avec l'ID: " + id));
        noteRepository.delete(note);
    }

    public Map<String, Object> calculerMoyenneEtudiant(Long etudiantId, Integer semestre) {
        List<Note> notes = noteRepository.findByEtudiantIdAndSemestre(etudiantId, semestre);
        
        if (notes.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("moyenne", BigDecimal.ZERO);
            result.put("nombreNotes", 0);
            return result;
        }

        BigDecimal somme = notes.stream()
                .map(Note::getValeur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal moyenne = somme.divide(BigDecimal.valueOf(notes.size()), 2, RoundingMode.HALF_UP);

        Map<String, Object> result = new HashMap<>();
        result.put("moyenne", moyenne);
        result.put("nombreNotes", notes.size());
        result.put("notes", notes);

        return result;
    }

    public Map<String, Object> genererBulletin(Long etudiantId, Integer semestre) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));

        List<Note> notes = noteRepository.findByEtudiantIdAndSemestre(etudiantId, semestre);
        Map<String, Object> moyenneData = calculerMoyenneEtudiant(etudiantId, semestre);

        Map<String, Object> bulletin = new HashMap<>();
        bulletin.put("etudiant", Map.of(
            "id", etudiant.getId(),
            "nom", etudiant.getNom(),
            "prenom", etudiant.getPrenom(),
            "filiere", etudiant.getFiliere(),
            "niveau", etudiant.getNiveau()
        ));
        bulletin.put("semestre", semestre);
        bulletin.put("notes", notes);
        bulletin.put("moyenne", moyenneData.get("moyenne"));
        bulletin.put("nombreNotes", moyenneData.get("nombreNotes"));

        String mention = calculerMention((BigDecimal) moyenneData.get("moyenne"));
        bulletin.put("mention", mention);

        return bulletin;
    }

    private String calculerMention(BigDecimal moyenne) {
        if (moyenne.compareTo(BigDecimal.valueOf(16)) >= 0) {
            return "Très Bien";
        } else if (moyenne.compareTo(BigDecimal.valueOf(14)) >= 0) {
            return "Bien";
        } else if (moyenne.compareTo(BigDecimal.valueOf(12)) >= 0) {
            return "Assez Bien";
        } else if (moyenne.compareTo(BigDecimal.valueOf(10)) >= 0) {
            return "Passable";
        } else {
            return "Insuffisant";
        }
    }
}
