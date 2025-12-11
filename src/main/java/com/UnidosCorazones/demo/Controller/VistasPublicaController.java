package com.UnidosCorazones.demo.Controller;


import com.UnidosCorazones.demo.Model.Beneficiario;
import com.UnidosCorazones.demo.Model.Campania;

import com.UnidosCorazones.demo.Respository.CampaniaRepository;
import com.UnidosCorazones.demo.Service.BeneficiarioService;
import com.UnidosCorazones.demo.Service.CampaniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/public")
public class VistasPublicaController {

    @Autowired
    private CampaniaService campaniaService;

    @Autowired
    private BeneficiarioService beneficiarioService;

    @GetMapping("/inicio") // La URL será: localhost:8080/public/inicio
    public String mostrarInicio(Model model) {

        // 2. Obtenemos las campañas y beneficiarios de la BD
        List<Campania> campañas = campaniaService.getUltimas3Campanias();
        List<Beneficiario> beneficiarios = beneficiarioService.getUltimosBeneficiarios();

        // 3. Los mandamos a la vista
        model.addAttribute("campanias", campañas);
        model.addAttribute("beneficiarios", beneficiarios);

        return "inicio"; // Busca inicio.html en templates
    }

    @GetMapping("/nosotros")
    public String mostrarNosotros(Model model) {
        return "nosotros";
    }


    @GetMapping("/actividades")
    public String mostrarPaginaActividades(Model model) {

        // 1. Obtiene solo las campañas públicas desde el servicio
        List<Campania> campañasPublicas = campaniaService.getCampaniasPublicas();

        // 2. Las agrega al modelo para que Thymeleaf las use
        model.addAttribute("campanias", campañasPublicas);

        // 3. Retorna el nombre del archivo HTML (que ahora será un template)
        return "actividades"; // Esto busca "actividades.html" en /resources/templates/
    }
}
