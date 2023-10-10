package edu.mtisw.monolithicwebapp.entities;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut;
    private String apellidos;
    private String nombres;
    private String fechaNacimiento;
    private String tipoColegioProcedencia;
    private String nombreColegio;
    private int anioEgresoColegio;
    private Double matricula;
    private Double arancel;

    private Double puntajePromedio;

    private Double promedioPuntajes;
    private int anioEgreso;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puntajes_id")
    private PuntajesEntity puntajesPruebas_;


    @Column(name = "puntaje_promedio_pruebas")
    private Double puntajePromedioPruebas = 0.0; // Inicialización del campo en el constructor

    @OneToOne(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PuntajesEntity puntajesEntity;

    // Anotación para definir la relación con las cuotas de pago
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CuotaPagoEntity> cuotasPago;

    // Agregar la lista de puntajes de pruebas
    @ElementCollection
    @CollectionTable(name = "puntajes_pruebas", joinColumns = @JoinColumn(name = "estudiante_id"))
    @Column(name = "puntaje")
    private List<Integer> puntajesPruebas;



    public void setPuntajePromedioPruebas(Double puntajePromedioPruebas) {
        this.puntajePromedioPruebas = puntajePromedioPruebas;
    }

    public double getPuntajePromedioPruebas() {
        if (puntajesPruebas_ != null) {
            List<Double> puntajes = puntajesPruebas_.getPuntajesPruebas();
            if (!puntajes.isEmpty()) {
                double suma = 0.0;
                for (Double puntaje : puntajes) {
                    suma += puntaje;
                }
                return suma / puntajes.size();
            }
        }
        return 0.0; // Manejo de caso vacío
    }


}
