package edu.mtisw.monolithicwebapp;



import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.entities.SubirDataEntity;
import edu.mtisw.monolithicwebapp.repositories.SubirDataRepository;
import edu.mtisw.monolithicwebapp.services.EstudianteService;
import edu.mtisw.monolithicwebapp.services.SubirDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SubirDataServiceTest {
    @InjectMocks
    private SubirDataService subirDataService;

    @Mock
    private SubirDataRepository subirDataRepository;

    @Mock
    private EstudianteService estudianteService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Captor
    private ArgumentCaptor<EstudianteEntity> estudianteCaptor;

    @Captor
    private ArgumentCaptor<List<SubirDataEntity>> dataCaptor;

    @Test
    public void testGuardar() {
        MultipartFile file = new MockMultipartFile("data", "DATA.TXT", "text/plain", "some xml".getBytes());
        String response = subirDataService.guardar(file);
        assertEquals("Archivo guardado con éxito!", response);
    }
    @Test
    public void testObtenerData() {
        when(subirDataRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<SubirDataEntity> response = subirDataService.obtenerData();
        assertEquals(0, response.size());
    }

    @Test
    public void testObtenerEspecifico() {
        when(subirDataRepository.findByRutAndFechaExamen(anyString(), anyString())).thenReturn(Optional.of(new SubirDataEntity()));
        Optional<SubirDataEntity> response = subirDataService.obtenerEspecifico("123", "2023-10-13");
        assertTrue(response.isPresent());
    }
    @Test
    public void testActualizarPromedioPuntajes() {
        when(estudianteService.getEstudianteByRut(anyString())).thenReturn(new EstudianteEntity());
        subirDataService.actualizarPromedioPuntajes("123", 90.0);

        Mockito.verify(estudianteService).saveStudent(estudianteCaptor.capture());
        EstudianteEntity capturedEstudiante = estudianteCaptor.getValue();

        // Aquí puedes hacer aserciones adicionales sobre el estudiante capturado si es necesario
        assertEquals(90.0, capturedEstudiante.getPuntajePromedio());
    }

    @Test
    public void testEliminarData() {
        ArrayList<SubirDataEntity> datas = new ArrayList<>();
        datas.add(new SubirDataEntity());
        subirDataService.eliminarData(datas);
        Mockito.verify(subirDataRepository).deleteAll(anyList());
    }



    @Test
    public void testCalcularPromedioPuntajes() {
        List<SubirDataEntity> puntajes = new ArrayList<>();
        puntajes.add(new SubirDataEntity(){{
            setPuntajeObtenido(80);
        }});
        puntajes.add(new SubirDataEntity(){{
            setPuntajeObtenido(90);
        }});
        when(subirDataRepository.findByRut(anyString())).thenReturn(puntajes);
        double promedio = subirDataService.calcularPromedioPuntajes("123");
        assertEquals(85.0, promedio);
    }





}
