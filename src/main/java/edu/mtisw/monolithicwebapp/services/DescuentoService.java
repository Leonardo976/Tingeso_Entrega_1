package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.repositories.DescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;

    @Autowired
    public DescuentoService(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }


    public List<DescuentoEntity> obtenerTodosLosDescuentos() {
        return descuentoRepository.findAll();
    }

    public DescuentoEntity obtenerDescuentoPorId(Long id) {
        return descuentoRepository.findById(id).orElse(null);
    }

    public void guardarDescuento(DescuentoEntity descuento) {
        descuentoRepository.save(descuento);
    }

    public void eliminarDescuento(Long id) {
        descuentoRepository.deleteById(id);
    }

}
