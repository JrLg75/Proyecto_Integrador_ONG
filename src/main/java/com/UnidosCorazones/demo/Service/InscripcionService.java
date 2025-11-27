package com.UnidosCorazones.demo.Service;

import com.UnidosCorazones.demo.Model.Administrador;
import com.UnidosCorazones.demo.Model.Campania;
import com.UnidosCorazones.demo.Model.Inscripcion;
import com.UnidosCorazones.demo.Model.Voluntario;
import com.UnidosCorazones.demo.Respository.CampaniaRepository;
import com.UnidosCorazones.demo.Respository.InscripcionRepository;
import com.UnidosCorazones.demo.Respository.AdministradorRepository;
import com.UnidosCorazones.demo.Respository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private CampaniaRepository campaniaRepository;

    public void inscribirse(String correoVoluntario, Integer idCampania) throws  Exception {

        Voluntario voluntario = voluntarioRepository.findByCorreo(correoVoluntario)
                .orElseThrow(() -> new RuntimeException("No existe voluntario con el correo: " + correoVoluntario));

        Campania campania = campaniaRepository.findByIdCampania(idCampania)
                .orElseThrow(() -> new Exception("Campaña no encontrada"));

        //Validar duplicadpos
        if (inscripcionRepository.existsByVoluntarioAndCampania(voluntario, campania)) {
            throw  new Exception("Ya estas inscrito a esta Campaña");
        }

        Inscripcion inscripcion = new Inscripcion();

        inscripcion.setVoluntario(voluntario);
        inscripcion.setCampania(campania);
        inscripcion.setFecha_inscripcion(LocalDateTime.now());

        inscripcionRepository.save(inscripcion);
    }

    public List<Integer> obtenerIdsInscripciones(String correoVoluntario) {
        Voluntario v = voluntarioRepository.findByCorreo(correoVoluntario).orElse(null);
        if(v == null) return new ArrayList<>();

        return inscripcionRepository.findByVoluntario(v).stream()
                .map(i -> i.getCampania().getIdCampania()) // Asumiendo que tu ID en campaña es id_campania
                .collect(Collectors.toList());
    }



}
