package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.CuotaPagoRepository;
import edu.mtisw.monolithicwebapp.repositories.DescuentoRepository;
import edu.mtisw.monolithicwebapp.services.CuotaPagoService;
import edu.mtisw.monolithicwebapp.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
