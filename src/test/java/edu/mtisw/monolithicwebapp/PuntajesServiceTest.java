package edu.mtisw.monolithicwebapp;

import edu.mtisw.monolithicwebapp.services.PuntajesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PuntajesServiceTest {

    @InjectMocks
    private PuntajesService puntajesService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerPuntajesPorRut() {

        String rut = "123456789-10";
        List<Double> expectedPuntajes = Arrays.asList(75.0, 80.0, 85.0);
        String sql = "SELECT puntaje_promedio_pruebas FROM data WHERE rut = :rut";

        // Configurar los mocks
        when(entityManager.createNativeQuery(sql)).thenReturn(query);
        when(query.setParameter("rut", rut)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedPuntajes);

        // Ejecutar
        List<Double> actualPuntajes = puntajesService.obtenerPuntajesPorRut(rut);

        // Verificar
        assertEquals(expectedPuntajes, actualPuntajes);
        verify(entityManager, times(1)).createNativeQuery(sql);
        verify(query, times(1)).setParameter("rut", rut);
        verify(query, times(1)).getResultList();
    }
}