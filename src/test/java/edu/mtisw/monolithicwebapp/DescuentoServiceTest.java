package edu.mtisw.monolithicwebapp;

import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.repositories.DescuentoRepository;
import edu.mtisw.monolithicwebapp.services.DescuentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DescuentoServiceTest {

    @Mock
    private DescuentoRepository descuentoRepository;

    @InjectMocks
    private DescuentoService descuentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDescuentosByRutEstudiante() {
        // Crear una lista de descuentos de ejemplo
        List<DescuentoEntity> descuentos = new ArrayList<>();
        DescuentoEntity descuento = new DescuentoEntity();
        // Configurar el descuento según tus necesidades
        descuento.setRutEstudiante("9967492-k");
        descuento.setPorcentajeDescuento(10.0); // Porcentaje de descuento deseado
        descuentos.add(descuento);

        // Configurar el comportamiento del repositorio mock
        when(descuentoRepository.findByRutEstudiante("9967492-k")).thenReturn(descuentos);

        // Llamar al método del servicio que quieres probar
        List<DescuentoEntity> result = descuentoService.getDescuentosByRutEstudiante("9967492-k");

        // Verificar que el servicio devuelve los descuentos esperados
        assertEquals(descuentos, result);
    }

    @Test
    public void testGetDescuentosByRutEstudianteWhenNoneFound() {
        // Configurar el comportamiento del repositorio mock cuando no se encuentran descuentos
        when(descuentoRepository.findByRutEstudiante("20596339-1")).thenReturn(new ArrayList<>());

        // Llamar al método del servicio que quieres probar cuando no se encuentran descuentos
        List<DescuentoEntity> result = descuentoService.getDescuentosByRutEstudiante("20596339-1");

        // Verificar que el servicio devuelve una lista vacía
        assertEquals(0, result.size());
    }
}