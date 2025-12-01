package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Model.Beneficiario;
import com.UnidosCorazones.demo.Service.BeneficiarioService;
import com.UnidosCorazones.demo.Service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/public")
public class RegistroBeneficiarioController {
    @Autowired
    private BeneficiarioService beneficiarioService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/registro-beneficiario")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("beneficiario", new Beneficiario());
        return "registro-beneficiario";
    }

    @PostMapping("/registro-beneficiario/guardar")
    public String procesarRegistro(
            @ModelAttribute Beneficiario beneficiario,
            @RequestParam("archivoDni") MultipartFile archivoDni,
            @RequestParam("archivoApoyo") MultipartFile archivoApoyo
    ) {
        // Guardar DNI
        if (!archivoDni.isEmpty()) {
            String nombreArchivo = storageService.store(archivoDni);
            beneficiario.setUrlCapturaDni("/media/" + nombreArchivo); // camelCase
        }

        // Guardar Documento de Apoyo
        if (!archivoApoyo.isEmpty()) {
            String nombreArchivo = storageService.store(archivoApoyo);
            beneficiario.setUrlDocumentoApoyo("/media/" + nombreArchivo); // camelCase
        }

        // Guardar en BD (El servicio debe encargarse de poner fecha y estado "Pendiente")
        beneficiarioService.registrarSolicitud(beneficiario);

        return "redirect:/public/registro-beneficiario";
    }
}
