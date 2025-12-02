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
        // Obtenemos el email si está logueado, o null si es invitado
        String email = (principal != null) ? principal.getName() : null;

        // El servicio nos devuelve un objeto vacío o pre-llenado
        Donacion donacion = donacionService.prepararNuevaDonacion(email);

        model.addAttribute("donacion", donacion);
        return "donaciones-form"; // Nombre de tu vista HTML
    }

    @PostMapping("/procesar")
    public String procesarDonacion(@ModelAttribute("donacion") Donacion donacion,
                                   Principal principal,
                                   RedirectAttributes redirectAttrs) {

        String email = (principal != null) ? principal.getName() : null;

        try {
            donacionService.registrarDonacion(donacion, email);
            redirectAttrs.addFlashAttribute("exito", "¡Gracias por tu donación! Tu aporte ha sido registrado.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Hubo un error al procesar la donación.");
            return "redirect:/donaciones/donar";
        }

        return "redirect:/donaciones/donar"; // O redirigir a una página de agradecimiento
    }
}