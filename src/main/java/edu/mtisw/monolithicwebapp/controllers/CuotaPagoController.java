package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.CuotaPagoRepository;
import edu.mtisw.monolithicwebapp.repositories.DescuentoRepository;
import edu.mtisw.monolithicwebapp.services.CuotaPagoService;
import edu.mtisw.monolithicwebapp.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cuotas-pago")
public class CuotaPagoController {
    private final CuotaPagoRepository cuotaPagoRepository;
    @Autowired
    private CuotaPagoService cuotaPagoService;
    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    public CuotaPagoController(
            CuotaPagoRepository cuotaPagoRepository,
            CuotaPagoService cuotaPagoService,
            EstudianteService estudianteService,
            DescuentoRepository descuentoRepository
    ) {
        this.cuotaPagoRepository = cuotaPagoRepository;
        this.cuotaPagoService = cuotaPagoService;
        this.estudianteService = estudianteService;
        this.descuentoRepository = descuentoRepository;
    }

    @PostMapping("/")
    public CuotaPagoEntity createCuotaPago(@RequestBody CuotaPagoEntity cuotaPago) {
        EstudianteEntity estudiante = cuotaPago.getEstudiante();

        // Utiliza el repositorio para obtener los descuentos
        List<DescuentoEntity> descuentos = descuentoRepository.findByAnioEgresoAndTipoColegioProcedencia(estudiante.getAnioEgreso(), estudiante.getTipoColegioProcedencia());

        // Imprime los descuentos obtenidos
        for (DescuentoEntity descuento : descuentos) {
            System.out.println("Descuento obtenido: " + descuento);
        }

        double montoCuotaSinDescuento = cuotaPago.getMonto();
        double montoTotalConDescuento = montoCuotaSinDescuento;

        for (DescuentoEntity descuento : descuentos) {
            double porcentajeDescuento = descuento.getPorcentajeDescuento() / 100.0;
            montoTotalConDescuento -= (montoCuotaSinDescuento * porcentajeDescuento);
        }

        cuotaPago.setMonto(montoTotalConDescuento);

        return cuotaPagoRepository.save(cuotaPago);
    }

    // Endpoint para obtener todas las cuotas de pago
    @GetMapping("/")
    public ArrayList<CuotaPagoEntity> getAllCuotasPago() {
        return (ArrayList<CuotaPagoEntity>) cuotaPagoRepository.findAll();
    }

    // Endpoint para obtener una cuota de pago por su ID
    @GetMapping("/{id}")
    public Optional<CuotaPagoEntity> getCuotaPagoById(@PathVariable Long id) {
        return cuotaPagoRepository.findById(id);
    }

    // Endpoint para eliminar una cuota de pago por su ID
    @DeleteMapping("/{id}")
    public void deleteCuotaPago(@PathVariable Long id) {
        cuotaPagoRepository.deleteById(id);
    }

    @PutMapping("/aplicar-descuento/{estudianteId}")
    public ResponseEntity<String> aplicarDescuentoAEstudiante(@PathVariable Long estudianteId, @RequestBody List<DescuentoEntity> descuentos) {
        // Obtener el estudiante por su ID
        EstudianteEntity estudiante = estudianteService.getEstudianteById(estudianteId);

        if (estudiante != null) {
            // Llamar al servicio para aplicar el descuento
            cuotaPagoService.aplicarDescuentoAEstudiante(estudiante, descuentos);
            return ResponseEntity.ok("Descuento aplicado con éxito al estudiante con ID: " + estudianteId);
        } else {
            // Manejar el caso en el que no se encuentra el estudiante por el ID
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear-cuota-pago")
    public ResponseEntity<String> crearCuotaPago(
            @RequestParam("numeroCuota") Integer numeroCuota,
            @RequestParam("estudianteId") Long estudianteId) {

        // Buscar el estudiante por ID
        EstudianteEntity estudiante = estudianteService.getEstudianteById(estudianteId);

        if (estudiante == null) {
            return ResponseEntity.badRequest().body("Estudiante no encontrado");
        }

        // Calcular el monto de la cuota con los descuentos correspondientes
        double montoCalculado = cuotaPagoService.calcularMontoCuotaConDescuentos(estudiante);

        // Crear una nueva instancia de CuotaPagoEntity con el monto calculado
        CuotaPagoEntity nuevaCuota = new CuotaPagoEntity();
        nuevaCuota.setNumeroCuota(numeroCuota);
        nuevaCuota.setMonto(montoCalculado); // Guarda el monto calculado con descuentos
        nuevaCuota.setEstudiante(estudiante);

        // Guardar la cuota de pago en la base de datos
        cuotaPagoService.createCuotaPago(nuevaCuota, estudiante);

        return ResponseEntity.ok("Cuota de pago creada exitosamente");
    }





}
