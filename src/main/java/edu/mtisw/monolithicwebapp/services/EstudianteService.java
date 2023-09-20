package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

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


}
