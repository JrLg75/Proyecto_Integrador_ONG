package com.UnidosCorazones.demo.Respository;

import com.UnidosCorazones.demo.Model.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Integer> {
    // Metodo de JPA para filtrar por el campo estadoRegistro
    List<Beneficiario> findByEstadoRegistro(String estadoRegistro);

}
