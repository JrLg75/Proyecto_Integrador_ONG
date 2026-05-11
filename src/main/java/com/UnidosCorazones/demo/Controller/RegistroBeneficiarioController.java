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
            @RequestParam(value = "archivoApoyo", required = false) MultipartFile[] archivosApoyo
    ) {
        // Guardar DNI
        if (!archivoDni.isEmpty()) {
            String nombreArchivo = storageService.store(archivoDni);
            beneficiario.setUrlCapturaDni("/media/" + nombreArchivo); // camelCase
        }

        // 2. Guardar Documentos de Apoyo (Múltiples)
        if (archivosApoyo != null && archivosApoyo.length > 0) {
            StringBuilder rutas = new StringBuilder();

            for (MultipartFile archivo : archivosApoyo) {
                if (!archivo.isEmpty()) {
                    String nombreGuardado = storageService.store(archivo);

                    // Si ya hay una ruta, agregamos un separador (;)
                    if (rutas.length() > 0) {
                        rutas.append(";");
                    }
                    rutas.append("/media/").append(nombreGuardado);
                }
            }
            // Guardamos la cadena concatenada en la BD (ej: "/media/a.pdf;/media/b.jpg")
            beneficiario.setUrlDocumentoApoyo(rutas.toString());
        }

        // Guardar en BD (El servicio debe encargarse de poner fecha y estado "Pendiente")
        beneficiarioService.registrarSolicitud(beneficiario);

        return "redirect:/inicio.html?registroExitoso=true";
    }
}
