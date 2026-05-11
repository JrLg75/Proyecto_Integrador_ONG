package com.UnidosCorazones.demo.Repository;

import com.UnidosCorazones.demo.Model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador,Integer> {
    Optional<Administrador> findByCorreo(String correo);
}