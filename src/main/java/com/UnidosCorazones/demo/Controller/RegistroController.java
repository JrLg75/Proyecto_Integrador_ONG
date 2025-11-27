package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Model.Voluntario;
import com.UnidosCorazones.demo.Service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    // Este método GET sirve para cargar la página inicialmente
    @GetMapping("/login-voluntario")
    public String mostrarLogin() {
        return "acceso-voluntario"; // Asegúrate que el HTML se llame así
    }

    @PostMapping("/registro-voluntario")
    public String registrarVoluntario(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("dni") String dni,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("disponibilidad") String disponibilidad,
            @RequestParam("habilidades") String habilidades,
            Model model,
            RedirectAttributes redirectAttributes) { // Agregamos RedirectAttributes

        System.out.println("--- INTENTO DE REGISTRO RECIBIDO ---");

        try {
            registroService.registrarVoluntario(nombre, apellido, dni, telefono, correo, contrasena, disponibilidad, habilidades);

            System.out.println("--- REGISTRO EXITOSO, REDIRIGIENDO ---");
            // ÉXITO: Usamos flash attribute para que sobreviva al redirect
            redirectAttributes.addFlashAttribute("registroExitoso", true);

            return "redirect:/login-voluntario";

        } catch (Exception e) {

            System.out.println("--- ERROR CAPTURADO: " + e.getMessage() + " ---");
            // ERROR: No hacemos redirect, nos quedamos en la misma petición para mostrar el error

            // 1. Pasamos el mensaje con el nombre EXACTO que espera el HTML
            model.addAttribute("errorRegistro", "Error: " + e.getMessage());

            // 2. Activamos la clase CSS para que el panel de registro siga visible
            model.addAttribute("mostrarPanelRegistro", true);

            // 3. Devolvemos los datos ingresados para que el usuario no tenga que escribir todo de nuevo
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("dni", dni);
            model.addAttribute("telefono", telefono);
            model.addAttribute("correo", correo);
            model.addAttribute("disponibilidad", disponibilidad);
            model.addAttribute("habilidades", habilidades);

            return "acceso-voluntario"; // Retornamos la vista, NO redirect
        }
    }
}
