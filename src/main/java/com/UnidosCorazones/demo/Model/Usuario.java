package com.UnidosCorazones.demo.Model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)//Estrategia de Herencia
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(unique = true, nullable = false, length = 120)
    private String correo;

    @Column(nullable = false)
    private String contrasena; // Spring Security se encargará de esto

    @Column(length = 15)
    private String telefono;

    @Column(name = "doc_identidad", unique = true, nullable = false, length = 20)
    private String docIdentidad;

    // Este campo nos sirve como discriminador para saber qué tipo es
    @Column(nullable = false, length = 20)
    private String tipo_usuario;



    // Un usuario puede hacer muchas donaciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("usuario-donaciones")
    private Set<Donacion> donaciones;


}
