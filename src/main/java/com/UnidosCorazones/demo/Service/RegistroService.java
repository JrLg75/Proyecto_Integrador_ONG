package com.UnidosCorazones.demo.Service;

import com.UnidosCorazones.demo.Model.Voluntario;
import com.UnidosCorazones.demo.Respository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

    @Autowired
    private VoluntarioRepository voluntarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Voluntario registrarVoluntario(String nombre, String apellido, String dni, String telefono, String correo,
                                          String contrasena, String disponibilidad, String habilidades) throws Exception {

        // CORRECCIÓN: Usar .isPresent() gracias al Optional del repositorio
        if (voluntarioRepository.findByCorreo(correo).isPresent()) {
            throw new Exception("El correo electrónico ya está registrado.");
        }
        if (voluntarioRepository.findByDocIdentidad(dni).isPresent()) {
            throw new Exception("El número de DNI ya está registrado en el sistema.");
        }

        String contrasenaCodificada = passwordEncoder.encode(contrasena);

        Voluntario voluntario = new Voluntario();
        voluntario.setNombre(nombre);
        voluntario.setApellido(apellido);
        voluntario.setTelefono(telefono);
        voluntario.setCorreo(correo);
        voluntario.setContrasena(contrasenaCodificada);
        voluntario.setTipo_usuario("Voluntario");
        voluntario.setDisponibilidad(disponibilidad);
        voluntario.setHabilidades(habilidades);

        return voluntarioRepository.save(voluntario);
    }
}