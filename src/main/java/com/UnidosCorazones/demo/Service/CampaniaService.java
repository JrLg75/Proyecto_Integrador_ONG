package com.UnidosCorazones.demo.Service;

import com.UnidosCorazones.demo.Model.Administrador;
import com.UnidosCorazones.demo.Model.Campania;
import com.UnidosCorazones.demo.Respository.AdministradorRepository;
import com.UnidosCorazones.demo.Respository.CampaniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CampaniaService {
    @Autowired
    private CampaniaRepository campaniaRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    /**
     * Obtiene las campañas separadas por estado (activas/pasadas) para el dashboard.
     */
    public Map<String, List<Campania>> getCampaniasParaDashboard() {
        List<Campania> activas = campaniaRepository.findByEstadoIn(List.of("activa", "proxima"));
        List<Campania> pasadas = campaniaRepository.findByEstadoIn(List.of("finalizada", "cancelada"));

        // Usamos un Map para devolver ambos resultados de forma ordenada
        Map<String, List<Campania>> dashboardData = new HashMap<>();
        dashboardData.put("campaniasActivas", activas);
        dashboardData.put("campaniasPasadas", pasadas);

        return dashboardData;
    }

    /**
     * Busca una campaña por su ID.
     * Lanza una excepción si no la encuentra.
     */
    public Campania findCampaniaById(Integer id) {
        return campaniaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de campaña inválido:" + id));
    }

    /**
     * Prepara una nueva instancia de Campania (para el formulario "nuevo").
     */
    public Campania inicializarNuevaCampania() {
        // En el futuro, aquí podrías asignar valores por defecto
        // ej: newCampania.setEstado("proxima");
        return new Campania();
    }

    /**
     * Guarda una campaña (ya sea nueva o una actualización).
     * Aquí se centraliza la lógica de guardado.
     */
    public void saveCampania(Campania campania, String correoDelAdmin) {
        // --- Lógica de ejemplo para asociar al admin ---
        // Aquí deberías obtener al administrador autenticado (ej. desde Spring Security)
        // Administrador adminLogueado = ...
        // campania.setAdministrador(adminLogueado);
        // ------------------------------------------------

        // ¡Usamos el método corregido!
        Administrador adminLogueado = administradorRepository.findByCorreo(correoDelAdmin)
                .orElseThrow(() -> new RuntimeException("No se encontró un perfil de Administrador para el correo: " + correoDelAdmin));

        // Asigna el administrador a la campaña
        campania.setAdministrador(adminLogueado);

        // Guarda la campaña
        campaniaRepository.save(campania);
        campaniaRepository.save(campania);
    }

    /**
     * Elimina una campaña por su ID.
     * Valida la existencia antes de borrar.
     */
    public void deleteCampaniaById(Integer id) {
        // Usamos el método findById para asegurarnos de que existe antes de borrar
        Campania campania = findCampaniaById(id);
        campaniaRepository.delete(campania);
    }

    public List<Campania> getCampaniasPublicas() {
        // Llama al nuevo método del repositorio
        return campaniaRepository.findByVisibleTrueAndEstado("activa");
    }

    public List<Campania> getUltimas3Campanias() {
        // Buscamos solo las que estén "activa"
        return campaniaRepository.findTop3ByEstadoAndVisibleTrueOrderByFechaInicioDesc("activa");
    }
}
