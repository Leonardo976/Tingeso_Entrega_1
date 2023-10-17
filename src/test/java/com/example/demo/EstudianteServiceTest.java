package com.example.demo;

import com.example.demo.entities.EstudianteEntity;
import com.example.demo.repositories.EstudianteRepository;
import com.example.demo.services.CuotaPagoService;
import com.example.demo.services.EstudianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstudianteServiceTest {

    @InjectMocks
    private EstudianteService estudianteService;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private CuotaPagoService cuotaPagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void obtenerPorIdTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(1L);
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        Optional<EstudianteEntity> result = estudianteService.obtenerPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    public void getAllEstudiantesTest() {
        EstudianteEntity estudiante1 = new EstudianteEntity();
        EstudianteEntity estudiante2 = new EstudianteEntity();
        List<EstudianteEntity> estudiantes = List.of(estudiante1, estudiante2);

        when(estudianteRepository.findAll()).thenReturn(estudiantes);

        List<EstudianteEntity> result = estudianteService.getAllEstudiantes();

        assertEquals(2, result.size());
        verify(estudianteRepository, times(1)).findAll();
    }


    @Test
    void saveStudentTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        when(estudianteRepository.save(any())).thenReturn(estudiante);

        EstudianteEntity savedStudent = estudianteService.saveStudent(new EstudianteEntity());

        assertNotNull(savedStudent);
    }

    @Test
    void eliminarEstudianteTest() {
        Long id = 1L;
        doNothing().when(estudianteRepository).deleteById(id);

        boolean result = estudianteService.eliminarEstudiante(id);

        assertTrue(result);
    }

    @Test
    void actualizarPuntajesTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(1L);
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(estudianteRepository.save(any(EstudianteEntity.class))).thenReturn(estudiante); // Asegúrate de que save no devuelve null

        EstudianteEntity updatedStudent = estudianteService.actualizarPuntajes(1L, Arrays.asList(50, 60, 70));

        assertNotNull(updatedStudent);
        assertEquals(Arrays.asList(50, 60, 70), updatedStudent.getPuntajesPruebas());
    }


    @Test
    void buscarEstudiantePorRutTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("12345678-9");
        when(estudianteRepository.findByRut("12345678-9")).thenReturn(estudiante);

        Optional<EstudianteEntity> result = estudianteService.buscarEstudiantePorRut("12345678-9");

        assertTrue(result.isPresent());
        assertEquals("12345678-9", result.get().getRut());
    }

    @Test
    void obtenerIdEstudiantePorRutTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("12345678-9");
        estudiante.setId(1L);
        when(estudianteRepository.findByRut("12345678-9")).thenReturn(estudiante);

        Long id = estudianteService.obtenerIdEstudiantePorRut("12345678-9");

        assertEquals(1L, id);
    }

    @Test
    void calcularPromedioServiceTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setPuntajesPruebas(Arrays.asList(70, 75, 80)); // Asegurándonos de que hay puntajes para calcular el promedio
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        double promedio = estudianteService.calcularPromedioService(1L);

        System.out.println("Promedio calculado en la prueba: " + promedio); // registro para depuración

        assertEquals(75.0, promedio, 0.01); // usando un delta para comparaciones de números de punto flotante
    }


    @Test
    void obtenerEstudiantesTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setPuntajesPruebas(Arrays.asList(50, 60, 70));
        when(estudianteRepository.findAll()).thenReturn(Arrays.asList(estudiante));

        List<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();

        // Log para depuración
        System.out.println("Puntajes de pruebas: " + estudiante.getPuntajesPruebas());
        System.out.println("Promedio calculado: " + estudiante.getPuntajePromedioPruebas());

        assertEquals(1, estudiantes.size());
        assertEquals(60.0, estudiantes.get(0).getPuntajePromedioPruebas());
    }


    @Test
    void guardarEstudiantesDesdeCSVTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setPuntajesPruebas(Arrays.asList(50, 60, 70));

        estudianteService.guardarEstudiantesDesdeCSV(Arrays.asList(estudiante));

        verify(estudianteRepository, times(1)).saveAll(any());
    }

    @Test
    void obtenerEstudiantesConPuntajesTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setPuntajesPruebas(Arrays.asList(50, 60, 70));
        when(estudianteRepository.findAll()).thenReturn(Arrays.asList(estudiante));

        List<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantesConPuntajes();

        assertEquals(1, estudiantes.size());
        assertEquals(60.0, estudiantes.get(0).getPuntajePromedioPruebas());
    }

    @Test
    void getEstudianteByIdTest() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(1L);
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        EstudianteEntity result = estudianteService.getEstudianteById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getEstudianteByIdNotFoundTest() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            estudianteService.getEstudianteById(1L);
        });

        assertEquals("No se encontró el estudiante con ID: 1", exception.getMessage());
    }
}
