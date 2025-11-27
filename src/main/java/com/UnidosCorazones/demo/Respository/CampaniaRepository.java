package com.UnidosCorazones.demo.Respository;

import com.UnidosCorazones.demo.Model.Campania;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaniaRepository extends JpaRepository<Campania, Integer> {
    List<Campania> findByVisibleTrue();
    List<Campania> findByEstadoIn(List<String> estados);
    List<Campania> findByVisibleTrueAndEstado(String estado);

    Optional<Campania> findByIdCampania(Integer id);

}


