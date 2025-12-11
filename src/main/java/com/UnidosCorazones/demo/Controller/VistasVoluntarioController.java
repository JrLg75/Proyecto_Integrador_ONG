package com.UnidosCorazones.demo.Controller;


import com.UnidosCorazones.demo.Model.Beneficiario;
import com.UnidosCorazones.demo.Model.Campania;
import com.UnidosCorazones.demo.Respository.CampaniaRepository;
import com.UnidosCorazones.demo.Service.BeneficiarioService;
import com.UnidosCorazones.demo.Service.CampaniaService;
import com.UnidosCorazones.demo.Service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/voluntario")
public class VistasVoluntarioController {
    @Autowired
    private CampaniaRepository campaniaRepository;

    @Autowired
    private CampaniaService campaniaService;

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private BeneficiarioService  beneficiarioService;

    @GetMapping("/inicio")
    public String mostrarInicioVoluntario(Model model,Principal principal) {

        List<Campania> campañas = campaniaService.getUltimas3Campanias();
        List<Beneficiario> beneficiarios = beneficiarioService.getUltimosBeneficiarios();

        List<Integer> misInscripcionesIds = inscripcionService.obtenerIdsInscripciones(principal.getName());

        // 3. ¡IMPORTANTE! Agregarlos al modelo para que el HTML no de error de NULL
        model.addAttribute("campanias", campañas);
        model.addAttribute("beneficiarios", beneficiarios);
        model.addAttribute("misInscripcionesIds", misInscripcionesIds);

        return "inicio-voluntario";
    }

    @GetMapping("/actividades")
    public String mostrarPaginaActividades(Model model, Principal principal) {

        // 1. Obtiene solo las campañas públicas desde el servicio
        List<Campania> campañas = campaniaService.getCampaniasPublicas();

        // 2. Cargar los IDs de campañas donde ESTE usuario ya se inscribió
        // 'Principal' contiene el usuario logueado (correo)
        List<Integer> misInscripcionesIds = inscripcionService.obtenerIdsInscripciones(principal.getName());

        // 2. Las agrega al modelo para que Thymeleaf las use
        model.addAttribute("campanias", campañas);
        model.addAttribute("misInscripcionesIds", misInscripcionesIds);

        // 3. Retorna el nombre del archivo HTML (que ahora será un template)
        return "actividades-voluntario"; // Esto busca "actividades.html" en /resources/templates/
    }

    @PostMapping("/inscribirse")
    public String inscribirse(@RequestParam("idCampania") Integer idCampania,
                              Principal principal,
                              RedirectAttributes redirectAttrs) {
        try {
            inscripcionService.inscribirse(principal.getName(), idCampania);
            redirectAttrs.addFlashAttribute("exito", "¡Te has inscrito correctamente!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/voluntario/actividades";
    }
}
