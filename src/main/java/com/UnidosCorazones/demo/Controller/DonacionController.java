package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Model.Donacion;
import com.UnidosCorazones.demo.Service.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/donaciones")
public class DonacionController {

    @Autowired
    private DonacionService donacionService;

    @GetMapping("/donar")
    public String mostrarFormularioDonacion(Model model, Principal principal) {
        // 1. Verificamos si hay usuario (principal no es null)
        String email = (principal != null) ? principal.getName() : null;

        // 2. El servicio busca los datos del usuario y nos devuelve una Donacion con nombre y correo llenos
        // (Si es null, devuelve una vacía)
        Donacion donacionPreCargada = donacionService.prepararNuevaDonacion(email);

        // 3. Pasamos ese objeto a la vista HTML
        model.addAttribute("datosPre", donacionPreCargada);

        return "donaciones-form"; // Asegúrate que este sea el nombre exacto de tu archivo HTML (sin .html)
    }

}