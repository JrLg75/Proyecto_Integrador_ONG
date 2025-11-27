package com.UnidosCorazones.demo;

import com.UnidosCorazones.demo.Model.Administrador;
import com.UnidosCorazones.demo.Model.Campania;
import com.UnidosCorazones.demo.Respository.AdministradorRepository;
import com.UnidosCorazones.demo.Respository.CampaniaRepository;
import com.UnidosCorazones.demo.Service.CampaniaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaniaServiceTest {

    @Mock
    private CampaniaRepository campaniaRepository;

    @Mock
    private AdministradorRepository administradorRepository;

    @InjectMocks
    private CampaniaService campaniaService;

    private Campania campania;
    private Administrador admin;

    @BeforeEach
    void setUp() {
        // --- Creamos un administrador simulado ---
        admin = new Administrador();
        admin.setId_usuario(1);
        admin.setCorreo("admin@test.com");
        admin.setNombre("Admin");
        admin.setApellido("Tester");
        admin.setTipo_usuario("administrador");

        // --- Creamos una campaña de ejemplo ---
        campania = new Campania();
        campania.setId_campania(1);
        campania.setTitulo("Campaña Test");
        campania.setDescripcion("Descripción de prueba");
        campania.setFecha_inicio(LocalDate.now());
        campania.setLugar("Lima");
        campania.setEstado("activa");
        campania.setVisible(true);
    }

    // -----------------------------------------------------------------
    // ✅ getCampaniasParaDashboard()
    // -----------------------------------------------------------------
    @Test
    void testGetCampaniasParaDashboard() {
        List<Campania> activas = List.of(campania);
        List<Campania> pasadas = List.of(new Campania());

        when(campaniaRepository.findByEstadoIn(List.of("activa", "proxima"))).thenReturn(activas);
        when(campaniaRepository.findByEstadoIn(List.of("finalizada", "cancelada"))).thenReturn(pasadas);

        Map<String, List<Campania>> result = campaniaService.getCampaniasParaDashboard();

        assertEquals(activas, result.get("campaniasActivas"));
        assertEquals(pasadas, result.get("campaniasPasadas"));
        verify(campaniaRepository, times(2)).findByEstadoIn(anyList());
    }

    // -----------------------------------------------------------------
    // ✅ findCampaniaById()
    // -----------------------------------------------------------------
    @Test
    void testFindCampaniaByIdFound() {
        when(campaniaRepository.findById(1)).thenReturn(Optional.of(campania));

        Campania result = campaniaService.findCampaniaById(1);

        assertNotNull(result);
        assertEquals(1, result.getId_campania());
        verify(campaniaRepository).findById(1);
    }

    @Test
    void testFindCampaniaByIdNotFound() {
        when(campaniaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> campaniaService.findCampaniaById(1));
    }

    // -----------------------------------------------------------------
    // ✅ saveCampania()
    // -----------------------------------------------------------------
    @Test
    void testSaveCampania() {
        when(administradorRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(admin));

        campaniaService.saveCampania(campania, "admin@test.com");

        assertEquals(admin, campania.getAdministrador());
        verify(administradorRepository).findByCorreo("admin@test.com");
        verify(campaniaRepository, times(2)).save(campania); // el método lo llama dos veces
    }

    @Test
    void testSaveCampaniaAdminNotFound() {
        when(administradorRepository.findByCorreo("admin@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> campaniaService.saveCampania(campania, "admin@test.com"));
        verify(campaniaRepository, never()).save(any());
    }

    // -----------------------------------------------------------------
    // ✅ deleteCampaniaById()
    // -----------------------------------------------------------------
    @Test
    void testDeleteCampaniaById() {
        when(campaniaRepository.findById(1)).thenReturn(Optional.of(campania));

        campaniaService.deleteCampaniaById(1);

        verify(campaniaRepository).delete(campania);
    }

    // -----------------------------------------------------------------
    // ✅ getCampaniasPublicas()
    // -----------------------------------------------------------------
    @Test
    void testGetCampaniasPublicas() {
        List<Campania> publicas = List.of(campania);
        when(campaniaRepository.findByVisibleTrueAndEstado("activa")).thenReturn(publicas);

        List<Campania> result = campaniaService.getCampaniasPublicas();

        assertEquals(1, result.size());
        assertEquals("Campaña Test", result.get(0).getTitulo());
        verify(campaniaRepository).findByVisibleTrueAndEstado("activa");
    }

    // -----------------------------------------------------------------
    // ✅ inicializarNuevaCampania()
    // -----------------------------------------------------------------
    @Test
    void testInicializarNuevaCampania() {
        Campania nueva = campaniaService.inicializarNuevaCampania();
        assertNotNull(nueva);
        assertNull(nueva.getId_campania());
    }
}

