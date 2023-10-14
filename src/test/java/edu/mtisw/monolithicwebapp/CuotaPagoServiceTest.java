package edu.mtisw.monolithicwebapp;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.CuotaPagoRepository;
import edu.mtisw.monolithicwebapp.repositories.EstudianteRepository;
import edu.mtisw.monolithicwebapp.services.CuotaPagoService;
import edu.mtisw.monolithicwebapp.services.DescuentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CuotaPagoServiceTest {

    @InjectMocks
    CuotaPagoService cuotaPagoService;

    @Mock
    CuotaPagoRepository cuotaPagoRepository;

    @Mock
    EstudianteRepository estudianteRepository;

    @Mock
    DescuentoService descuentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void calcularMontoCuotaConDescuentos() {
        // Given
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setTipoColegioProcedencia("Municipal");
        estudiante.setAnioEgresoColegio(2);

        // When
        double montoConDescuento = cuotaPagoService.calcularMontoCuotaConDescuentos(estudiante);

        // Then
        // Los descuentos son: 20% por colegio municipal y 8% por 2 años de egreso
        double expectedDescuento = (0.20 + 0.08);
        double montoOriginal = 1500000.0;
        double expectedMonto = montoOriginal - (montoOriginal * expectedDescuento);

        assertEquals(expectedMonto, montoConDescuento, 0.01);
    }




    @Test
    void actualizarMontosDeCuotasConDescuentos() {
        // Given
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setTipoColegioProcedencia("Municipal");
        estudiante.setAnioEgresoColegio(1);

        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setMonto(1500000.0);
        cuota.setEstudiante(estudiante);

        when(cuotaPagoRepository.findByEstudiante(estudiante)).thenReturn(Collections.singletonList(cuota));

        // When
        cuotaPagoService.actualizarMontosDeCuotasConDescuentos(estudiante);

        // Then
        // Aquí debes adaptar la verificación según tu lógica de negocio y los descuentos específicos
        verify(cuotaPagoRepository, times(1)).saveAll(anyList());
    }

    @Test
    void cambiarEstadoPago() {
        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setId(1L);
        cuota.setPagada(false);

        when(cuotaPagoRepository.findById(1L)).thenReturn(Optional.of(cuota));

        cuotaPagoService.cambiarEstadoPago(1L, "pagada");

        verify(cuotaPagoRepository).save(cuota);
        assertEquals(true, cuota.isPagada());
    }










    @Test
    void getCuotasByEstudiante() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(1L);

        List<CuotaPagoEntity> cuotas = new ArrayList<>();
        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setEstudiante(estudiante);
        cuotas.add(cuota);

        when(cuotaPagoRepository.findByEstudiante(estudiante)).thenReturn(cuotas);

        List<CuotaPagoEntity> retrievedCuotas = cuotaPagoService.getCuotasByEstudiante(estudiante);

        assertEquals(cuotas, retrievedCuotas);
    }



    @Test
    void actualizarCuotasConPuntajes() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setTipoColegioProcedencia("Municipal");
        estudiante.setAnioEgresoColegio(1);
        estudiante.setPuntajePromedioPruebas(900.0);

        List<CuotaPagoEntity> cuotas = new ArrayList<>();
        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setMonto(1500000.0);
        cuota.setEstudiante(estudiante);
        cuotas.add(cuota);

        when(cuotaPagoRepository.findByEstudiante(estudiante)).thenReturn(cuotas);

        cuotaPagoService.actualizarCuotasConPuntajes(estudiante);

        verify(cuotaPagoRepository).saveAll(cuotas);
        assertEquals(1080000.0, cuotas.get(0).getMonto());
    }

    @Test
    void aplicarInteresSobreCuotasPendientes() {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Restar 2 meses a la fecha actual
        LocalDate fechaVencimiento = fechaActual.minusMonths(2);

        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setFechaVencimiento(fechaVencimiento); // No es necesario convertir a java.sql.Date
        cuota.setMonto(1500000.0);
        cuota.setPagada(false);

        List<CuotaPagoEntity> cuotasPendientes = new ArrayList<>();
        cuotasPendientes.add(cuota);

        when(cuotaPagoRepository.findByPagadaFalse()).thenReturn(cuotasPendientes);

        cuotaPagoService.aplicarInteresSobreCuotasPendientes();

        verify(cuotaPagoRepository).save(cuota);
        assertEquals(1590000.0, cuota.getMonto());
    }

    @Test
    void getCuotasPendientesByEstudiante() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(1L);

        List<CuotaPagoEntity> cuotas = new ArrayList<>();
        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setPagada(false);
        cuotas.add(cuota);

        when(cuotaPagoRepository.findByEstudianteAndPagadaFalse(estudiante)).thenReturn(cuotas);

        List<CuotaPagoEntity> retrievedCuotas = cuotaPagoService.getCuotasPendientesByEstudiante(estudiante);

        assertEquals(cuotas, retrievedCuotas);
    }


    @Test
    void aplicarDescuentos() {
        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setMonto(1500000.0);
        cuota.setPagada(false);
        cuota.setEstudiante(null); // Establece el estudiante a null para probar el nuevo comportamiento

        List<CuotaPagoEntity> cuotasPendientes = new ArrayList<>();
        cuotasPendientes.add(cuota);

        when(cuotaPagoRepository.findByPagadaFalse()).thenReturn(cuotasPendientes);

        cuotaPagoService.aplicarDescuentos(new ArrayList<>());

        // Como el estudiante es null, no deberíamos intentar guardar la cuota actualizada
        verify(cuotaPagoRepository, never()).save(cuota);

        // Podrías añadir más aserciones aquí para asegurarte de que tu lógica se está ejecutando como esperas
        // Por ejemplo, podrías verificar que se ha registrado un mensaje de error o que no se intenta calcular el monto de la cuota
    }


    @Test
    void aplicarDescuentosACuotas() {
        String rutEstudiante = "12345678-9";
        DescuentoEntity descuento = new DescuentoEntity();
        List<DescuentoEntity> descuentosEstudiante = new ArrayList<>();
        descuentosEstudiante.add(descuento);

        CuotaPagoEntity cuota = new CuotaPagoEntity();
        cuota.setMonto(1500000.0);
        List<CuotaPagoEntity> cuotasEstudiante = new ArrayList<>();
        cuotasEstudiante.add(cuota);

        // Suponiendo que necesitas un método para obtener cuotas por el RUT del estudiante en tu repositorio
        when(cuotaPagoRepository.findByRutEstudiante(rutEstudiante)).thenReturn(cuotasEstudiante);

        // Aquí estoy asumiendo que tienes un método que obtiene descuentos por RUT del estudiante en tu DescuentoService
        when(descuentoService.getDescuentosByRutEstudiante(rutEstudiante)).thenReturn(descuentosEstudiante);

        cuotaPagoService.aplicarDescuentosACuotas(rutEstudiante);

        verify(cuotaPagoRepository).save(cuota);  // Verifica que el método save se ha llamado
    }
}








