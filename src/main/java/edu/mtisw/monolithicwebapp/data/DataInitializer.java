package edu.mtisw.monolithicwebapp.data;
import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.repositories.DescuentoRepository;
import edu.mtisw.monolithicwebapp.services.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private DescuentoRepository descuentoRepository;

    @Override
    public void run(String... args) throws Exception {
        DescuentoEntity descuento1 = new DescuentoEntity(null, "Tipo1", 5.5, 2022, 1000.00, 10.0, "14339271-6");
        DescuentoEntity descuento2 = new DescuentoEntity(null, "Tipo2", 6.0, 2023, 2000.00, 20.0, "9967492-k");

        descuentoRepository.save(descuento1);
        descuentoRepository.save(descuento2);
    }
}