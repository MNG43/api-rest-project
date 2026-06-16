package com.universite.apirest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "emploi_du_temps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploiDuTemps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek jour;

    @NotNull
    @Column(nullable = false)
    private LocalTime heureDebut;

    @NotNull
    @Column(nullable = false)
    private LocalTime heureFin;

    @Column(nullable = false)
    private Integer semestre;

    @Column(nullable = false)
    private Integer anneeAcademique;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
