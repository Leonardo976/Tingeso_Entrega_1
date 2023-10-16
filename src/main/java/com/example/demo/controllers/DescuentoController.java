package com.example.demo.controllers;

import com.example.demo.entities.DescuentoEntity;
import com.example.demo.entities.EstudianteEntity;
import com.example.demo.services.CuotaPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/descuentos")
public class DescuentoController {

    // Supongamos que esta lista contendrá los descuentos cargados

    private final CuotaPagoService cuotaPagoService; // Inyecta el servicio de CuotaPago
    private EstudianteEntity estudianteEntity;

    @Autowired
    public DescuentoController(CuotaPagoService cuotaPagoService) {
        this.cuotaPagoService = cuotaPagoService;
    }

    @PostMapping("/cargar")
    public String cargarDescuentos() {
        // Carga el archivo CSV desde la carpeta resources
        Resource resource = new ClassPathResource("descuentos.csv");
        List<DescuentoEntity> descuentos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    DescuentoEntity descuento = new DescuentoEntity();
                    descuento.setTipoColegioProcedencia(parts[0]);
                    descuento.setAnioEgreso(Integer.parseInt(parts[1]));
                    descuento.setPorcentajeDescuento(Double.parseDouble(parts[2]));
                    descuentos.add(descuento);
                }
            }

            // Aquí puedes llamar al servicio para aplicar los descuentos a las cuotas
            cuotaPagoService.aplicarDescuentos(descuentos);

            return "Descuentos cargados correctamente y aplicados a las cuotas.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al cargar los descuentos.";
        }
    }

    private double calcularMontoCuotaConDescuentos(double montoBaseCuota, List<DescuentoEntity> descuentos) {
        double montoConDescuento = montoBaseCuota;

        for (DescuentoEntity descuento : descuentos) {
            if (cumpleCondicionesDescuento(montoBaseCuota, descuento)) {
                // Aplicar el descuento al monto de la cuota
                double porcentajeDescuento = descuento.getPorcentajeDescuento();
                montoConDescuento *= (1 - (porcentajeDescuento / 100));
            }
        }

        return montoConDescuento;
    }

    private boolean cumpleCondicionesDescuento(double montoBaseCuota, DescuentoEntity descuento) {
        // Obtén el umbral de monto del descuento
        double umbralMonto = descuento.getUmbralMonto();

        // Comprueba si el monto base de la cuota supera el umbral
        if (montoBaseCuota >= umbralMonto) {
            // Si el monto base es mayor o igual al umbral, el descuento se aplica
            return true;
        } else {
            // Si no, el descuento no se aplica
            return false;
        }
    }
}
