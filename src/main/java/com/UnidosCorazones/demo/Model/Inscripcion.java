package com.UnidosCorazones.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripcion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_inscripcion;

    private LocalDateTime fecha_inscripcion;

    // Muchas inscripciones son para una campaña
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campania", nullable = false)
    @JsonBackReference("campania-inscripciones")
    private Campania campania;


    // Muchas inscripciones son de un voluntario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voluntario",  nullable = false)
    @JsonBackReference("voluntario-inscripciones")
    private Voluntario voluntario;


}
