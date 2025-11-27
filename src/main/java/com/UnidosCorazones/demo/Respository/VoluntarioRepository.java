package com.UnidosCorazones.demo.Respository;

import com.UnidosCorazones.demo.Model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Integer> {
    // Cambiar List por Optional para mejor manejo de nulos/existencia
    Optional<Voluntario> findByCorreo(String correo);
    Optional<Voluntario> findByDocIdentidad(String docI);
}