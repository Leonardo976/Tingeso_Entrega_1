package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.services.CuotaPagoService;
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
    private List<DescuentoEntity> descuentos = new ArrayList<>();

    private final CuotaPagoService cuotaPagoService; // Inyecta el servicio de CuotaPago

    @Autowired
    public DescuentoController(CuotaPagoService cuotaPagoService) {
        this.cuotaPagoService = cuotaPagoService;
    }

    @PostMapping("/cargar")
    public String cargarDescuentos() {
        // Carga el archivo CSV desde la carpeta resources
        Resource resource = new ClassPathResource("descuentos.csv");

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
}
