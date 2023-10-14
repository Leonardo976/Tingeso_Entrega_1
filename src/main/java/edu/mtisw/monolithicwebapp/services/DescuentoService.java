package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.repositories.DescuentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DescuentoService {

    private final Logger logger = LoggerFactory.getLogger(DescuentoService.class);

    private final DescuentoRepository descuentoRepository;

    @Autowired
    public DescuentoService(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }

    public List<DescuentoEntity> getDescuentosByRutEstudiante(String rut) {
        List<DescuentoEntity> descuentos = descuentoRepository.findByRutEstudiante(rut);
        if (descuentos.isEmpty() && logger != null) { // Also added null check for logger
            logger.warn("No descuentos found for rutEstudiante: {}", rut);
        }
        return descuentos;
    }
}
