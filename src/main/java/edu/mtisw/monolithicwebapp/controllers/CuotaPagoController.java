package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.repositories.CuotaPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cuotas-pago")
public class CuotaPagoController {
    private final CuotaPagoRepository cuotaPagoRepository;

    @Autowired
    public CuotaPagoController(CuotaPagoRepository cuotaPagoRepository) {
        this.cuotaPagoRepository = cuotaPagoRepository;
    }

    // Endpoint para crear una nueva cuota de pago
    @PostMapping("/")
    public CuotaPagoEntity createCuotaPago(@RequestBody CuotaPagoEntity cuotaPago) {
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

    @GetMapping("/estudiante/{rut}")
    public List<CuotaPagoEntity> getCuotasPagoByEstudianteRut(@PathVariable String rut) {
        return cuotaPagoRepository.findByEstudiante_Rut(rut);
    }


}
