package com.UnidosCorazones.demo;

import com.UnidosCorazones.demo.Model.Usuario;
import com.UnidosCorazones.demo.Respository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Importaciones estáticas para MockMvc y Spring Security
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    /**
     * Prueba que las páginas públicas sean accesibles sin autenticación.
     */
    @Test
    void PaginasPublicasAccessible() throws Exception {
        // 1. Probar la página de login
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk()) // Espera un 200 OK
                .andExpect(view().name("login")); // Espera que se retorne la vista "login"

        // 2. Probar la redirección de la raíz
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection()) // Espera una redirección (302)
                .andExpect(redirectedUrl("/inicio.html")); // Espera que redirija a /inicio.html
    }

    /**
     * Prueba que una página protegida (p.ej. /admin/campanias) redirija al
     * login si el usuario no está autenticado.
     */
    @Test
    void PaginaProtegidaRedirigeALoginSiNoEstaAutenticado() throws Exception {
        mockMvc.perform(get("/admin/campanias"))
                .andExpect(status().is3xxRedirection()) //Espera redireccion
                .andExpect(redirectedUrl("http://localhost/login")); // Espera que redirija a la pág. de login
    }

    /**
     * Prueba un inicio de sesión exitoso.
     * Simula el envío del formulario de login.
     */
    @Test
    void LoginExitoso() throws Exception {
        // 1. Configurar el Mock:
        // Creamos un usuario admin de prueba
        Usuario mockAdmin = new Usuario();
        mockAdmin.setCorreo("admin@test.com");
        mockAdmin.setContrasena("adminpass"); // La contraseña en texto plano (por NoOpPasswordEncoder)
        mockAdmin.setTipo_usuario("ADMINISTRADOR");

        // Cuando el UserDetailsService llame a findByCorreo, devolvemos nuestro mock
        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(mockAdmin));

        // 2. Ejecutar la prueba:
        // Usamos formLogin() de spring-security-test.
        // Automáticamente usa tu loginProcessingUrl ("/admin-login") y los parámetros.
        mockMvc.perform(formLogin("/admin-login").user("admin@test.com").password("adminpass"))
                .andExpect(status().is3xxRedirection()) // Espera redirección
                .andExpect(redirectedUrl("/admin/campanias")) // Espera que redirija a la URL de éxito
                .andExpect(authenticated().withUsername("admin@test.com").withRoles("ADMINISTRADOR")); // Verifica que está autenticado con el rol correcto
    }

    /**
     * Prueba un inicio de sesión fallido (contraseña incorrecta).
     */
    @Test
    void LoginFallidoContraseñaIncorrecta() throws Exception {
        // 1. Configurar el Mock (el usuario SÍ existe)
        Usuario mockAdmin = new Usuario();
        mockAdmin.setCorreo("admin@test.com");
        mockAdmin.setContrasena("adminpass"); // Esta es la contraseña correcta
        mockAdmin.setTipo_usuario("ADMINISTRADOR");

        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(mockAdmin));

        // 2. Ejecutar la prueba (pero enviamos la contraseña INCORRECTA)
        mockMvc.perform(formLogin("/admin-login").user("admin@test.com").password("wrongpass"))
                .andExpect(status().is3xxRedirection()) // Espera redirección
                .andExpect(redirectedUrl("/login?error")) // Espera que redirija a la pág. de login CON error
                .andExpect(unauthenticated()); // Verifica que NO está autenticado
    }

    /**
     * Prueba un inicio de sesión fallido (usuario no existe).
     */
    @Test
    void LoginFallidoUsuarioNoExiste() throws Exception {
        // 1. Configurar el Mock (el usuario NO existe)
        when(usuarioRepository.findByCorreo("nouser@test.com")).thenReturn(Optional.empty());

        // 2. Ejecutar la prueba
        mockMvc.perform(formLogin("/admin-login").user("nouser@test.com").password("somepass"))
                .andExpect(status().is3xxRedirection()) // Espera redirección
                .andExpect(redirectedUrl("/login?error")) // Espera que redirija a la pág. de login CON error
                .andExpect(unauthenticated()); // Verifica que NO está autenticado
    }

    /**
     * Prueba la autorización.
     * Simula un usuario YA AUTENTICADO con el rol correcto.
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRADOR"}) // Simula un usuario logueado
    void AutorizacionUsuarioRolCorrecto() throws Exception {
        mockMvc.perform(get("/admin/campanias"))
                .andExpect(status().isOk()); // Debe poder acceder (200 OK)
    }

    /**
     * Prueba la autorización fallida (Forbidden).
     * Simula un usuario YA AUTENTICADO pero con un ROL INCORRECTO.
     */
    @Test
    @WithMockUser(username = "user", roles = {"VOLUNTARIO"}) // Rol incorrecto
    void AutorizacionFallidaRolIncorrecto() throws Exception {
        mockMvc.perform(get("/admin/campanias"))
                .andExpect(status().isForbidden()); // Debe ser 403 Forbidden
    }

    /**
     * Prueba un cierre de sesión (logout) exitoso.
     */
    @Test
    @WithMockUser // Simula cualquier usuario logueado
    void LogoutExitoso() throws Exception {
        mockMvc.perform(post("/logout").with(csrf())) // El logout es un POST y requiere CSRF
                .andExpect(status().is3xxRedirection()) // Espera redirección
                .andExpect(redirectedUrl("/login?logout")) // A la página de logout exitoso
                .andExpect(unauthenticated()); // Verifica que ya no está autenticado
    }


}
