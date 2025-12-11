package com.UnidosCorazones.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "campania")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Campania {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campania")
    private Integer idCampania;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fecha_fin;

    @Column(nullable = false, length = 200)
    private String lugar;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "imagen_url", length = 500) // Nueva columna
    private String imagenUrl;

    private Boolean visible;

    // Muchas campañas pertenecen a un administrador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin", nullable = false)
    @JsonBackReference("admin-campanias")
    private Administrador administrador;

    // Una campaña tiene muchas inscripciones
    @OneToMany(mappedBy = "campania", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("campania-inscripciones")
    private Set<Inscripcion> inscripciones;
}
