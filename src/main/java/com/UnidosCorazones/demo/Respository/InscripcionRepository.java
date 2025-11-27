package com.UnidosCorazones.demo.Respository;

import com.UnidosCorazones.demo.Model.Campania;
import com.UnidosCorazones.demo.Model.Inscripcion;
import com.UnidosCorazones.demo.Model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion,Integer> {
    // Para validar si ya existe la inscripción
    boolean existsByVoluntarioAndCampania(Voluntario voluntario, Campania campania);

    // Para saber a qué campañas ya está inscrito el voluntario (y pintar el botón diferente)
    List<Inscripcion> findByVoluntario(Voluntario voluntario);
}
