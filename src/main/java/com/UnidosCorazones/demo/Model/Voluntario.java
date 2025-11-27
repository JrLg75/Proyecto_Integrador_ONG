package com.UnidosCorazones.demo.Model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "voluntario")
@PrimaryKeyJoinColumn(name = "id_voluntario")
@Getter
@Setter
public class Voluntario extends Usuario {
    @Column(length = 100)
    private String disponibilidad;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @OneToMany(mappedBy = "voluntario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("voluntario-inscripciones")
    private Set<Inscripcion> inscripciones;

}
