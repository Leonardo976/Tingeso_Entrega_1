package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    private CuotaPagoService cuotaPagoService;


    public ArrayList<EstudianteEntity> obtenerEstudiantes(){
        return (ArrayList<EstudianteEntity>) estudianteRepository.findAll();
    }
    public Optional<EstudianteEntity> obtenerPorId(Long id){
        return estudianteRepository.findById(id);
    }

    public boolean eliminarEstudiante(Long id) {
        try{
            estudianteRepository.deleteById(id);
            return true;
        }catch(Exception err){
            return false;
        }
    }

    public ArrayList<EstudianteEntity> getAllEstudiantes() {
        return (ArrayList<EstudianteEntity>) estudianteRepository.findAll();
    }

    public EstudianteEntity saveStudent(EstudianteEntity estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public Optional<EstudianteEntity> buscarEstudiantePorRut(String rut) {
        // Implementa la lógica para buscar un estudiante por su Rut
        // Puedes usar el método findByRut de tu EstudianteRepository si tienes uno
        // Ejemplo (si tienes un método findByRut en el repositorio):
        return Optional.ofNullable(estudianteRepository.findByRut(rut));
    }




}
