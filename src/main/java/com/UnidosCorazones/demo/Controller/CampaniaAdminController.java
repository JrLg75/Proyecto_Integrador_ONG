package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Model.Campania;
import com.UnidosCorazones.demo.Respository.CampaniaRepository;
import com.UnidosCorazones.demo.Service.CampaniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/campanias")
public class CampaniaAdminController {

    // ¡Inyectamos el Servicio, no el Repositorio!
    @Autowired
    private CampaniaService campaniaService;

    // READ: Muestra la lista de campañas
    @GetMapping
    public String mostrarListaCampanias(Model model) {
        // 1. Pedimos los datos al servicio
        Map<String, List<Campania>> dashboardData = campaniaService.getCampaniasParaDashboard();

        // 2. Pasamos los datos del Map al modelo
        model.addAttribute("campaniasActivas", dashboardData.get("campaniasActivas"));
        model.addAttribute("campaniasPasadas", dashboardData.get("campaniasPasadas"));

        return "admin-campanias";
    }

    // CREATE (FORM): Muestra el formulario vacío
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        // El servicio nos da el objeto "vacío"
        model.addAttribute("campania", campaniaService.inicializarNuevaCampania());
        model.addAttribute("pageTitle", "Agregar Nueva Campaña");
        return "admin-form-campania";
    }

    // UPDATE (FORM): Muestra el formulario lleno para editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model) {
        // El servicio se encarga de buscar (o fallar)
        Campania campania = campaniaService.findCampaniaById(id);


        model.addAttribute("campania", campania);
        model.addAttribute("pageTitle", "Editar Campaña: " + campania.getTitulo());
        return "admin-form-campania";
    }

    // CREATE / UPDATE (LOGIC): Guarda la campaña
    @PostMapping("/guardar")
    public String guardarCampania(@ModelAttribute("campania") Campania campania,
                                  @RequestParam(value = "visible", required = false) Boolean visible,
                                  Principal principal) { // <-- Esto sigue igual

        campania.setVisible(visible != null && visible);

        String correo = principal.getName(); // <-- Esto sigue dándote el correo

        // Esto ahora llama al servicio actualizado
        campaniaService.saveCampania(campania, correo);

        return "redirect:/admin/campanias";
    }

    // DELETE: Elimina la campaña
    @PostMapping("/eliminar")
    public String eliminarCampania(@RequestParam("id_campania") Integer id) {
        // El servicio se encarga de la lógica de borrado (y validación)
        campaniaService.deleteCampaniaById(id);

        return "redirect:/admin/campanias";
    }

    // RECIBE EL ID DE LA CAMPAÑA Y MUESTRA LA LISTA DE INSCRITOS
    @GetMapping("/{id}/inscritos")
    public String verInscritos(@PathVariable("id") Integer id, Model model) {
        // Se busca la campaña
        Campania campania = campaniaService.findCampaniaById(id);

        // Se pasa la campaña al modelo
        model.addAttribute("campania", campania);
        model.addAttribute("inscripciones", campania.getInscripciones());

        return "admin-campania-inscritos"; // Nueva vista que crearemos
    }
}