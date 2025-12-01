package com.UnidosCorazones.demo.Service;

import com.UnidosCorazones.demo.Model.Beneficiario;
import com.UnidosCorazones.demo.Respository.BeneficiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BeneficiarioService {
    @Autowired
    private BeneficiarioRepository beneficiarioRepository;

    // Registra una nueva solicitud pública
    // Asigna automáticamente la fecha actual y el estado "Pendiente"
    public void registrarSolicitud(Beneficiario beneficiario) {
        beneficiario.setFechaSolicitud(LocalDateTime.now());
        beneficiario.setEstadoRegistro("Pendiente");

        beneficiarioRepository.save(beneficiario);
    }

    // Obtiene listas separadas para el panel de administración
    public Map<String, List<Beneficiario>> getBeneficiariosPorEstado() {
        List<Beneficiario> pendientes = beneficiarioRepository.findByEstadoRegistro("Pendiente");
        List<Beneficiario> validados = beneficiarioRepository.findByEstadoRegistro("Validado");
        List<Beneficiario> rechazados = beneficiarioRepository.findByEstadoRegistro("Rechazado");

        Map<String, List<Beneficiario>> datos = new HashMap<>();
        datos.put("pendientes", pendientes);
        datos.put("validados", validados);
        datos.put("rechazados", rechazados);
        return datos;
    }

    // Cambia el estado de una solicitud (Aceptar/Rechazar)
    public void cambiarEstado(Integer id, String nuevoEstado) {
        Beneficiario b = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiario no encontrado"));

        b.setEstadoRegistro(nuevoEstado);
        if ("Validado".equals(nuevoEstado)) {
            b.setFechaAprobacion(LocalDateTime.now());
        }
        beneficiarioRepository.save(b);
    }

}
