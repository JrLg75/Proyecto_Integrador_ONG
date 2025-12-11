package com.UnidosCorazones.demo.Service;

import com.UnidosCorazones.demo.Model.Donacion;
import com.UnidosCorazones.demo.Model.Usuario;
import com.UnidosCorazones.demo.Respository.DonacionRepository;
import com.UnidosCorazones.demo.Respository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonacionService {

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Asumo que tienes este repo

    public Donacion prepararNuevaDonacion(String emailUsuarioLogueado) {
        Donacion donacion = new Donacion();

        // Si hay un usuario logueado, pre-llenamos los datos
        if (emailUsuarioLogueado != null) {
            Usuario usuario = usuarioRepository.findByCorreo(emailUsuarioLogueado)
                    .orElse(null);; // Ajusta según tu método de búsqueda
            if (usuario != null) {
                donacion.setNombreDonante(usuario.getNombre() + " " + usuario.getApellido());
                donacion.setCorreoContacto(usuario.getCorreo());
                donacion.setUsuario(usuario);
            }
        }
        return donacion;
    }

    @Transactional
    public void registrarDonacion(String nombre, String emailContacto, BigDecimal monto, String emailUsuarioLogueado) {
        Donacion donacion = new Donacion();

        // 1. Datos básicos
        donacion.setNombreDonante(nombre);
        donacion.setCorreoContacto(emailContacto);
        donacion.setMonto(monto);
        donacion.setFecha(LocalDateTime.now());
        donacion.setEstado("APROBADO"); // Asumimos éxito porque lo llamamos post-PayPal

        // 2. Vincular usuario si existe sesión (Spring Security)
        if (emailUsuarioLogueado != null && !emailUsuarioLogueado.equals("anonymousUser")) {
            Usuario usuario = usuarioRepository.findByCorreo(emailUsuarioLogueado).orElse(null);
            donacion.setUsuario(usuario);
        } else {
            donacion.setUsuario(null);
        }

        // 3. Guardar
        donacionRepository.save(donacion);
    }

    // Devuelve todas las donaciones ordenadas por fecha descendente
    public List<Donacion> obtenerTodas() {
        return donacionRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
    }
}
