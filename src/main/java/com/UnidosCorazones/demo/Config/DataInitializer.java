package com.UnidosCorazones.demo.Config;

import com.UnidosCorazones.demo.Model.Administrador;
import com.UnidosCorazones.demo.Model.Usuario;
import com.UnidosCorazones.demo.Respository.AdministradorRepository;
import com.UnidosCorazones.demo.Respository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificamos si ya existe el admin para no duplicarlo
            if (administradorRepository.findByCorreo("admin@ong.com").isEmpty()) {

                Administrador admin = new Administrador();
                admin.setNombre("Super");
                admin.setApellido("Admin");
                admin.setCorreo("admin@ong.com");
                // AQUÍ está la magia: Spring encripta la contraseña antes de guardar
                admin.setContrasena(passwordEncoder.encode("admin123"));
                admin.setTelefono("999888777");
                admin.setDocIdentidad("00000001");
                admin.setTipo_usuario("Administrador"); // O el valor que uses para administradores
                admin.setCargo("Primer Administrador");

                administradorRepository.save(admin);
                System.out.println("--> Admin inicial creado: admin@ong.com / admin123");
            }
        };
    }
}
