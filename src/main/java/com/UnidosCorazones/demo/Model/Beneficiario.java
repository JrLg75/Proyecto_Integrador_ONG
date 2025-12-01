package com.UnidosCorazones.demo.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiario")
@Getter
@Setter
public class Beneficiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBeneficiario;

    // DATOS DE IDENTIFICACIÓN
    @Column(name = "nombre_completo_o_razon", nullable = false, length = 150)
    private String nombreCompletoORazon; // CamelCase aquí

    @Column(name = "tipo_documento", length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(name = "tipo_beneficiario", length = 20)
    private String tipoBeneficiario;

    @Column(name = "cantidad_personas_afectadas")
    private Integer cantidadPersonasAfectadas;

    // DATOS DE CONTACTO
    @Column(length = 9)
    private String celular;

    @Column(length = 100)
    private String correo;

    @Column(length = 255)
    private String direccion;

    // DATOS DE LA SOLICITUD
    @Column(name = "justificacion_solicitud", columnDefinition = "TEXT", nullable = false)
    private String justificacionSolicitud;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "estado_registro", nullable = false, length = 20)
    private String estadoRegistro;

    // DOCUMENTACIÓN (Rutas)

    @Column(name = "url_captura_dni")
    private String urlCapturaDni;

    @Column(name = "url_documento_apoyo")
    private String urlDocumentoApoyo;


}
