package com.universite.apirest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @NotBlank
    @Column(nullable = false)
    private String type; // INSCRIPTION, SCOLARITE

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private String methodePaiement; // WAVE, ORANGE_MONEY, ESPECES, CARTE

    @Column(nullable = false)
    private String statut; // EN_ATTENTE, PAYE, ECHOUE

    @Column(columnDefinition = "TEXT")
    private String referenceTransaction;

    @Column(nullable = false)
    private Integer anneeAcademique;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;
}
