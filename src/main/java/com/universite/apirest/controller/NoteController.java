package com.universite.apirest.controller;

import com.universite.apirest.entity.Note;
import com.universite.apirest.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Note>> getNotesByEtudiant(@PathVariable Long etudiantId) {
        List<Note> notes = noteService.getNotesByEtudiant(etudiantId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/etudiant/{etudiantId}/semestre/{semestre}")
    public ResponseEntity<List<Note>> getNotesByEtudiantAndSemestre(
            @PathVariable Long etudiantId, 
            @PathVariable Integer semestre) {
        List<Note> notes = noteService.getNotesByEtudiantAndSemestre(etudiantId, semestre);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Note>> getNotesByCours(@PathVariable Long coursId) {
        List<Note> notes = noteService.getNotesByCours(coursId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/etudiant/{etudiantId}/cours/{coursId}")
    public ResponseEntity<?> getNoteByEtudiantAndCours(
            @PathVariable Long etudiantId, 
            @PathVariable Long coursId) {
        Optional<Note> note = noteService.getNoteByEtudiantAndCours(etudiantId, coursId);
        if (note.isPresent()) {
            return ResponseEntity.ok(note.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Note non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(note);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Note créée avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @Valid @RequestBody Note note) {
        try {
            Note updatedNote = noteService.updateNote(id, note);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Note modifiée");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Note supprimée");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/etudiant/{etudiantId}/moyenne/semestre/{semestre}")
    public ResponseEntity<?> calculerMoyenneEtudiant(
            @PathVariable Long etudiantId, 
            @PathVariable Integer semestre) {
        Map<String, Object> moyenne = noteService.calculerMoyenneEtudiant(etudiantId, semestre);
        return ResponseEntity.ok(moyenne);
    }

    @GetMapping("/etudiant/{etudiantId}/bulletin/semestre/{semestre}")
    public ResponseEntity<?> genererBulletin(
            @PathVariable Long etudiantId, 
            @PathVariable Integer semestre) {
        try {
            Map<String, Object> bulletin = noteService.genererBulletin(etudiantId, semestre);
            return ResponseEntity.ok(bulletin);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
