package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Service.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/donaciones")
public class DonacionAdminController {

    @Autowired
    private DonacionService donacionService;

    @GetMapping
    public String listarDonaciones(Model model) {
        model.addAttribute("donaciones", donacionService.obtenerTodas());
        return "admin-donaciones";
    }
}
