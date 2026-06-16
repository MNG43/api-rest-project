package com.universite.apirest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @NotBlank
    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String message;

    @NotBlank
    @Column(nullable = false)
    private String type; // NOTE, PAIEMENT, COURS, GENERAL

    @NotBlank
    @Column(nullable = false)
    private String canal; // EMAIL, SMS

    @Column(nullable = false)
    private boolean lu = false;

    @Column(nullable = false)
    private String statut; // EN_ATTENTE, ENVOYE, ECHOUE

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;
}
