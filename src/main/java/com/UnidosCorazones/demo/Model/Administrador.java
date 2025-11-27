package com.UnidosCorazones.demo.Model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "id_admin")//Une la PK de esta tabla con Usuario
@Getter
@Setter
public class Administrador extends Usuario {

    @Column(length = 100)
    private String cargo;

    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("admin-campanias")
    private Set<Campania> campanias;
}
