package com.universite.apirest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String prenom;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String sexe;
    @Column(nullable = false)
    private LocalDate dateNaissance;
    @Column(nullable = false)
    private String lieuNaissance;
    @Column(nullable = false)
    private String nationalite;
    @Column(nullable = false, unique = true)
    private String telephone;
    @Column(nullable = false)
    private String adresse;
    @Enumerated(EnumType.STRING)
    private Fillier filiere;
    @Enumerated(EnumType.STRING)
    private Niveau niveau;
    @Column(nullable = false)
    private static final String  universite="UADB";
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}