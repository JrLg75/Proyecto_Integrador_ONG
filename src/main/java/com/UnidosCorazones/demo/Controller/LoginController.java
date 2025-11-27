package com.UnidosCorazones.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login-admin")
    public String showLoginPage() {
        return "login-admin"; // Retorna el nombre del archivo
    }

    @GetMapping("/")
    public String redirectToHome() {
        // Redirige la raíz a la página de inicio pública
        return "redirect:/public/inicio";
    }


}
