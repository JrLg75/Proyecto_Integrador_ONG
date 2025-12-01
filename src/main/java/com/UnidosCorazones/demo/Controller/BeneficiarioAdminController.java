package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Model.Beneficiario;
import com.UnidosCorazones.demo.Service.BeneficiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/beneficiarios")
public class BeneficiarioAdminController {
    @Autowired
    private BeneficiarioService beneficiarioService;

    // Vista Principal con Tablas separadas
    @GetMapping
    public String listarBeneficiarios(Model model) {
        // Obtenemos el mapa con las 3 listas (Pendientes, Validados, Rechazados)
        Map<String, List<Beneficiario>> datos = beneficiarioService.getBeneficiariosPorEstado();

        model.addAttribute("pendientes", datos.get("pendientes"));
        model.addAttribute("validados", datos.get("validados"));
        model.addAttribute("rechazados", datos.get("rechazados"));

        return "admin-beneficiarios";
    }

    // Acción para Aprobar o Rechazar
    @PostMapping("/cambiar-estado")
    public String cambiarEstado(@RequestParam("id") Integer id,
                                @RequestParam("estado") String estado) {
        beneficiarioService.cambiarEstado(id, estado);
        return "redirect:/admin/beneficiarios";
    }
}
