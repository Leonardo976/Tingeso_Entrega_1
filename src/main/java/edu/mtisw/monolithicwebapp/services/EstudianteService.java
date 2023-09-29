package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    private CuotaPagoService cuotaPagoService;


    public List<EstudianteEntity> obtenerEstudiantes() {
        List<EstudianteEntity> estudiantes = (List<EstudianteEntity>) estudianteRepository.findAll();

        // Calcular el promedio de puntajes para cada estudiante
        for (EstudianteEntity estudiante : estudiantes) {
            List<Integer> puntajes = estudiante.getPuntajesPruebas();
            double promedio = calcularPromedioPuntajes(puntajes);
            estudiante.setPuntajePromedioPruebas(promedio);
        }

        return estudiantes;
    }

    private double calcularPromedioPuntajes(List<Integer> puntajesPruebas) {
        if (puntajesPruebas == null || puntajesPruebas.isEmpty()) {
            return 0.0;
        }

        int totalPuntajes = 0;
        for (Integer puntaje : puntajesPruebas) {
            totalPuntajes += puntaje;
        }

        // Calcula el promedio dividiendo la suma total de puntajes entre el número de puntajes
        return (double) totalPuntajes / puntajesPruebas.size();
    }
    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
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

    public EstudianteEntity getEstudianteByRut(String rut) {
        Optional<EstudianteEntity> estudiante = Optional.ofNullable(estudianteRepository.findByRut(rut));
        return estudiante.orElse(null);
    }

    // Método para actualizar los puntajes de pruebas de un estudiante
    public EstudianteEntity actualizarPuntajes(Long estudianteId, List<Integer> nuevosPuntajes) {
        Optional<EstudianteEntity> estudianteOptional = estudianteRepository.findById(estudianteId);

        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            estudiante.setPuntajesPruebas(nuevosPuntajes);

            // Aquí puedes realizar cualquier otra lógica relacionada con la actualización de puntajes

            return estudianteRepository.save(estudiante);
        } else {
            throw new RuntimeException("Estudiante no encontrado con ID: " + estudianteId);
        }
    }
    public void actualizarPromedioPuntajes(String rut, double promedio) {
        Optional<EstudianteEntity> estudianteOptional = Optional.ofNullable(estudianteRepository.findByRut(rut));
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            estudiante.setPuntajePromedioPruebas(promedio);
            estudianteRepository.save(estudiante);
        } else {
            // Si el estudiante no se encuentra, lanzar una excepción o manejar el error según tus necesidades.
            throw new EntityNotFoundException("Estudiante no encontrado con el rut: " + rut);
        }
    }





}
