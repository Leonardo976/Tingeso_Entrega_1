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


    @Column(name = "puntaje_promedio_pruebas")
    private Double puntajePromedioPruebas = 0.0; // Inicialización del campo en el constructor

    // Anotación para definir la relación con las cuotas de pago
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CuotaPagoEntity> cuotasPago;

    // Agregar la lista de puntajes de pruebas
    @ElementCollection
    @CollectionTable(name = "puntajes_pruebas", joinColumns = @JoinColumn(name = "estudiante_id"))
    @Column(name = "puntaje")
    private List<Integer> puntajesPruebas;


    public Double getPuntajePromedioPruebas() {
        return puntajePromedioPruebas;
    }

    public void setPuntajePromedioPruebas(Double puntajePromedioPruebas) {
        this.puntajePromedioPruebas = puntajePromedioPruebas;
    }


}
