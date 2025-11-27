package com.UnidosCorazones.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donacion")
@Getter
@Setter
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_donacion;

    @Column(nullable = false)
    private BigDecimal monto; // Usamos BigDecimal para dinero (NUMERIC)

    @Column(nullable = false, length = 50)
    private String metodo_pago;

    private LocalDateTime fecha; // Usamos LocalDateTime para TIMESTAMP

    @Column(nullable = false, length = 20)
    private String estado;

    // Muchas donaciones pertenecen a un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference("usuario-donaciones")
    private Usuario usuario;

}
