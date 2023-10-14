package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    private CuotaPagoService cuotaPagoService;



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

    public List<EstudianteEntity> getAllEstudiantes() {
        return (List<EstudianteEntity>) estudianteRepository.findAll();
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

    public EstudianteEntity actualizarPuntajes(Long estudianteId, List<Integer> nuevosPuntajes) {
        Optional<EstudianteEntity> estudianteOptional = estudianteRepository.findById(estudianteId);

        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            estudiante.setPuntajesPruebas(nuevosPuntajes);

            EstudianteEntity updatedEstudiante = estudianteRepository.save(estudiante);
            if (updatedEstudiante != null) {
                return updatedEstudiante;
            } else {
                throw new RuntimeException("Error al actualizar los puntajes del estudiante con ID: " + estudianteId);
            }
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

    public Long obtenerIdEstudiantePorRut(String rut) {
        EstudianteEntity estudiante = estudianteRepository.findByRut(rut);
        if (estudiante != null) {
            return estudiante.getId();
        } else {
            return null; // Puedes manejar el caso en el que no se encuentre el estudiante
        }
    }


    public double calcularPromedioService(Long estudianteId) {
        EstudianteEntity estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId));

        List<Integer> puntajes = estudiante.getPuntajesPruebas();

        if (puntajes == null || puntajes.isEmpty()) {
            throw new IllegalStateException("El estudiante con ID: " + estudianteId + " no tiene puntajes registrados");
        }

        double sum = puntajes.stream().mapToDouble(Integer::doubleValue).sum();
        double promedio = sum / puntajes.size();

        System.out.println("Promedio calculado: " + promedio); // registro para depuración

        estudiante.setPuntajePromedioPruebas(promedio);
        estudianteRepository.save(estudiante);

        return promedio;
    }



    public List<EstudianteEntity> obtenerEstudiantes() {
        List<EstudianteEntity> estudiantes = (List<EstudianteEntity>) estudianteRepository.findAll();

        for (EstudianteEntity estudiante : estudiantes) {
            List<Integer> puntajes = estudiante.getPuntajesPruebas();

            // Filtra los puntajes no numéricos y luego calcula el promedio
            List<Integer> puntajesNumericos = puntajes.stream()
                    .filter(p -> p != null) // Filtra los puntajes no nulos
                    .collect(Collectors.toList());

            double promedio = calcularPromedioPuntajes(puntajesNumericos);
            estudiante.setPuntajePromedioPruebas(promedio);
        }

        return estudiantes;
    }

    // Función para verificar si una cadena es numérica
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void guardarEstudiantesDesdeCSV(List<EstudianteEntity> estudiantes) {
        for (EstudianteEntity estudiante : estudiantes) {
            // Calcula el promedio de puntajes y establece el valor en la entidad del estudiante
            double promedio = calcularPromedioPuntajes(estudiante.getPuntajesPruebas());
            estudiante.setPuntajePromedioPruebas(promedio);
        }

        // Luego, guarda todos los estudiantes en la base de datos
        estudianteRepository.saveAll(estudiantes);
    }

    public List<EstudianteEntity> obtenerEstudiantesConPuntajes() {
        List<EstudianteEntity> estudiantes = (List<EstudianteEntity>) estudianteRepository.findAll();

        for (EstudianteEntity estudiante : estudiantes) {
            // Verifica si la entidad tiene puntajes
            if (estudiante.getPuntajesPruebas() != null) {
                List<Integer> puntajes = estudiante.getPuntajesPruebas();
                double promedio = calcularPromedioPuntajes(puntajes);
                // Formatea el promedio como una cadena con dos decimales
                String promedioFormateado = String.format("%.2f", promedio);
                estudiante.setPuntajePromedioPruebas(Double.valueOf(promedioFormateado));
            }
        }

        return estudiantes;
    }

    public EstudianteEntity getEstudianteById(Long id) {
        Optional<EstudianteEntity> estudianteOptional = estudianteRepository.findById(id);

        if (estudianteOptional.isPresent()) {
            return estudianteOptional.get();
        } else {
            // Manejar el caso en el que no se encuentra el estudiante por el ID
            throw new RuntimeException("No se encontró el estudiante con ID: " + id);
        }
    }




}
