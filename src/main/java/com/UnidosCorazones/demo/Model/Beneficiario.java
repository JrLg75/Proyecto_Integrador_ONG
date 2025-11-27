package com.UnidosCorazones.demo.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "benficiario")
@PrimaryKeyJoinColumn(name = "id_Beneficiario")
@Getter
@Setter
public class Beneficiario extends Usuario {
    @Column(name = "razonSocial", length = 200)
    private  String razonSocial;

    private LocalDate fecha_nacimiento;

    @Column(length = 20)
    private String estado_registro;

}
