package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.CuotaPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuotaPagoService {


    private final CuotaPagoRepository cuotaPagoRepository;
    private final List<DescuentoEntity> descuentos; // Lista de descuentos

    @Autowired
    public CuotaPagoService(CuotaPagoRepository cuotaPagoRepository) {
        this.cuotaPagoRepository = cuotaPagoRepository;
        this.descuentos = new ArrayList<>(); // Inicializa la lista de descuentos
    }

    public List<CuotaPagoEntity> getAllCuotasPago() {
        return cuotaPagoRepository.findAll();
    }

    public Optional<CuotaPagoEntity> getCuotaPagoById(Long id) {
        return cuotaPagoRepository.findById(id);
    }


    public void deleteCuotaPago(Long id) {
        cuotaPagoRepository.deleteById(id);
    }

    // Lógica para calcular el monto de la cuota con los descuentos correspondientes


    // Lógica para calcular el monto de la cuota con los descuentos correspondientes
    public double calcularMontoCuotaConDescuentos(EstudianteEntity estudiante) {
        String tipoColegio = estudiante.getTipoColegioProcedencia();
        int aniosEgreso = estudiante.getAnioEgresoColegio();

        // Calcular el descuento basado en el tipo de colegio de procedencia
        double descuentoTipoColegio = 0.0;
        if ("Municipal".equals(tipoColegio)) {
            descuentoTipoColegio = 0.20;
        } else if ("Subvencionado".equals(tipoColegio)) {
            descuentoTipoColegio = 0.10;
        } // No se aplica descuento para tipo "Privado"

        // Calcular el descuento basado en los años desde que egresó del colegio
        double descuentoAniosEgreso = 0.0;
        if (aniosEgreso < 1) {
            descuentoAniosEgreso = 0.15;
        } else if (aniosEgreso >= 1 && aniosEgreso <= 2) {
            descuentoAniosEgreso = 0.08;
        } else if (aniosEgreso >= 3 && aniosEgreso <= 4) {
            descuentoAniosEgreso = 0.04;
        } // No se aplica descuento para 5 o más años

        // Calcular el descuento basado en el puntaje promedio de pruebas
        double descuentoPuntajePromedio = calcularDescuentoPorPuntajePromedio(estudiante);

        // Sumar los descuentos
        double descuentoTotal = descuentoTipoColegio + descuentoAniosEgreso + descuentoPuntajePromedio;

        // Calcular el monto total de la cuota con los descuentos aplicados
        double montoTotalCuota = 1500000.0;  // Monto base del arancel de estudio
        double montoConDescuento = montoTotalCuota - (montoTotalCuota * descuentoTotal);

        return montoConDescuento;
    }


    // Lógica para obtener el número máximo de cuotas según el tipo de colegio de procedencia
    int obtenerNumeroMaximoCuotas(EstudianteEntity estudiante) {
        String tipoColegio = estudiante.getTipoColegioProcedencia();

        // Definir el número máximo de cuotas según el tipo de colegio de procedencia
        int maxCuotas = 0;
        if ("Municipal".equals(tipoColegio)) {
            maxCuotas = 10;
        } else if ("Subvencionado".equals(tipoColegio)) {
            maxCuotas = 7;
        } else if ("Privado".equals(tipoColegio)) {
            maxCuotas = 4;
        }

        return maxCuotas;
    }

    // Método para calcular el descuento basado en el puntaje promedio de pruebas
    private double calcularDescuentoPorPuntajePromedio(EstudianteEntity estudiante) {
        double puntajePromedio = estudiante.getPuntajePromedioPruebas();

        if (puntajePromedio >= 950 && puntajePromedio <= 1000) {
            return 0.10;  // Descuento del 10% para puntajes entre 950 y 1000
        } else if (puntajePromedio >= 900 && puntajePromedio < 950) {
            return 0.05;  // Descuento del 5% para puntajes entre 900 y 949
        } else if (puntajePromedio >= 850 && puntajePromedio < 900) {
            return 0.02;  // Descuento del 2% para puntajes entre 850 y 899
        } else {
            return 0.0;   // No se aplica descuento para puntajes menores a 850
        }
    }

    public void aplicarInteresSobreCuotasPendientes() {
        // Obtener todas las cuotas pendientes
        List<CuotaPagoEntity> cuotasPendientes = cuotaPagoRepository.findByPagadaFalse();

        // Obtener la fecha actual para calcular los meses de atraso
        LocalDate fechaActual = LocalDate.now();

        for (CuotaPagoEntity cuota : cuotasPendientes) {
            LocalDate fechaVencimiento = cuota.getFechaVencimiento();
            long mesesAtraso = ChronoUnit.MONTHS.between((Temporal) fechaVencimiento, fechaActual);

            // Aplicar el interés según la cantidad de meses de atraso
            double interes = calcularInteresPorMesesAtraso(mesesAtraso);

            // Calcular el nuevo monto con interés y actualizar la cuota
            double nuevoMonto = cuota.getMonto() * (1 + (interes / 100));
            cuota.setMonto(nuevoMonto);

            // Guardar la cuota actualizada
            cuotaPagoRepository.save(cuota);
        }
    }

    private double calcularInteresPorMesesAtraso(long mesesAtraso) {
        if (mesesAtraso == 0) {
            return 0.0;  // 0% de interés para 0 meses de atraso
        } else if (mesesAtraso == 1) {
            return 3.0;  // 3% de interés para 1 mes de atraso
        } else if (mesesAtraso == 2) {
            return 6.0;  // 6% de interés para 2 meses de atraso
        } else if (mesesAtraso == 3) {
            return 9.0;  // 9% de interés para 3 meses de atraso
        } else {
            return 15.0;  // 15% de interés para más de 3 meses de atraso
        }
    }

    // Método para calcular la fecha de vencimiento de una cuota basado en el número de cuota
    private LocalDate calcularFechaVencimiento(int numeroCuota) {
        // Supongamos que las cuotas se pagan mensualmente
        // Puedes ajustar la lógica según tus requisitos

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Calcular la fecha de vencimiento sumando meses al mes actual
        // Por ejemplo, aquí se suma el número de cuotas como meses a la fecha actual
        LocalDate fechaVencimiento = fechaActual.plusMonths(numeroCuota);

        return fechaVencimiento;
    }

    public List<CuotaPagoEntity> calcularYCrearCuotasParaEstudiante(EstudianteEntity estudiante) {
        // Calcular el número máximo de cuotas según el tipo de colegio de procedencia
        int maxCuotas = obtenerNumeroMaximoCuotas(estudiante);

        // Calcular el monto de cada cuota con los descuentos correspondientes
        double montoCalculado = calcularMontoCuotaConDescuentos(estudiante);

        List<CuotaPagoEntity> cuotasGeneradas = new ArrayList<>();

        // Crear y asociar las cuotas con el estudiante
        for (int numeroCuota = 1; numeroCuota <= maxCuotas; numeroCuota++) {
            CuotaPagoEntity cuotaPago = new CuotaPagoEntity();
            cuotaPago.setNumeroCuota(numeroCuota);
            cuotaPago.setMonto(montoCalculado);
            cuotaPago.setFechaVencimiento(calcularFechaVencimiento(numeroCuota)); // Aquí se pasa el número de cuota
            cuotaPago.setPagada(false);
            cuotaPago.setEstudiante(estudiante);

            // Guardar la cuota de pago en la base de datos
            cuotaPagoRepository.save(cuotaPago);

            cuotasGeneradas.add(cuotaPago);
        }

        return cuotasGeneradas;
    }

    public List<CuotaPagoEntity> getCuotasByEstudiante(EstudianteEntity estudiante) {
        return cuotaPagoRepository.findByEstudiante(estudiante);
    }

    // Método para calcular y actualizar cuotas basadas en los puntajes de pruebas
    public void actualizarCuotasConPuntajes(EstudianteEntity estudiante) {
        // Calcular el monto de cada cuota con los nuevos descuentos correspondientes
        double montoCalculado = calcularMontoCuotaConDescuentos(estudiante);

        List<CuotaPagoEntity> cuotasEstudiante = cuotaPagoRepository.findByEstudiante(estudiante);

        // Actualizar los montos de las cuotas existentes
        for (CuotaPagoEntity cuota : cuotasEstudiante) {
            cuota.setMonto(montoCalculado);
            // Puedes actualizar la fecha de vencimiento si es necesario
            // cuota.setFechaVencimiento(calcularFechaVencimiento(cuota.getNumeroCuota()));
        }

        // Guardar las cuotas actualizadas en la base de datos
        cuotaPagoRepository.saveAll(cuotasEstudiante);
    }

    public void cambiarEstadoPago(Long cuotaId, String nuevoEstado) {
        Optional<CuotaPagoEntity> optionalCuota = cuotaPagoRepository.findById(cuotaId);

        if (optionalCuota.isPresent()) {
            CuotaPagoEntity cuota = optionalCuota.get();
            cuota.setPagada("pagada".equalsIgnoreCase(nuevoEstado)); // Cambiar el estado de pago
            cuotaPagoRepository.save(cuota); // Guardar la cuota actualizada en la base de datos
        } else {
            // Manejar el caso en el que no se encuentra la cuota por el ID
            throw new RuntimeException("No se encontró la cuota con ID: " + cuotaId);
        }
    }


    // Método para aplicar descuentos a una cuota específica
    private double aplicarDescuentosACuota(CuotaPagoEntity cuota) {
        double montoConDescuento = cuota.getMonto();

        for (DescuentoEntity descuento : descuentos) {
            if (cumpleCondicionesDescuento(cuota.getEstudiante(), descuento)) {
                // Aplicar el descuento a esta cuota
                double porcentajeDescuento = descuento.getPorcentajeDescuento();
                montoConDescuento *= (1 - porcentajeDescuento);
            }
        }

        return montoConDescuento;
    }


    public DescuentoEntity buscarDescuentoAplicable(List<DescuentoEntity> descuentos, EstudianteEntity estudiante) {
        DescuentoEntity descuentoAplicable = null;

        for (DescuentoEntity descuento : this.descuentos) {
            if (cumpleCondicionesDescuento(estudiante, descuento)) {
                if (descuentoAplicable == null || descuento.getPorcentajeDescuento() > descuentoAplicable.getPorcentajeDescuento()) {
                    descuentoAplicable = descuento;
                }
            }
        }

        return descuentoAplicable;
    }

    private boolean cumpleCondicionesDescuento(EstudianteEntity estudiante, DescuentoEntity descuento) {
        // Obtener los datos relevantes del estudiante
        String tipoColegioEstudiante = estudiante.getTipoColegioProcedencia();
        int anioEgresoEstudiante = estudiante.getAnioEgresoColegio();

        // Obtener los datos relevantes del descuento
        String tipoColegioDescuento = descuento.getTipoColegioProcedencia();
        int anioEgresoDescuento = descuento.getAnioEgreso();

        // Verificar si las condiciones del descuento coinciden con los datos del estudiante
        return tipoColegioEstudiante.equals(tipoColegioDescuento) && anioEgresoEstudiante == anioEgresoDescuento;
    }

    public void aplicarDescuentoAEstudiante(EstudianteEntity estudiante, List<DescuentoEntity> descuentos) {
        // Lógica para aplicar los descuentos al estudiante
        double montoConDescuento = calcularMontoCuotaConDescuentos(estudiante);

        for (DescuentoEntity descuento : descuentos) {
            if (cumpleCondicionesDescuento(estudiante, descuento)) {
                // Aplicar el descuento al monto total
                double porcentajeDescuento = descuento.getPorcentajeDescuento();
                montoConDescuento *= (1 - (porcentajeDescuento / 100));
            }
        }


    }

    public List<CuotaPagoEntity> getCuotasPendientesByEstudiante(EstudianteEntity estudiante) {
        return cuotaPagoRepository.findByEstudianteAndPagadaFalse(estudiante);
    }


    public CuotaPagoEntity createCuotaPago(CuotaPagoEntity cuotaPago, EstudianteEntity estudiante) {
        // Calcular el monto de la cuota con los descuentos correspondientes
        double montoCalculado = calcularMontoCuotaConDescuentos(estudiante);

        // Establecer el monto calculado con descuentos en la cuota
        cuotaPago.setMonto(montoCalculado);

        // Verificar el número máximo de cuotas permitidas
        int maxCuotas = obtenerNumeroMaximoCuotas(estudiante);
        if (cuotaPago.getNumeroCuota() > maxCuotas) {
            // Manejar el error de cuotas excedidas
            throw new RuntimeException("Número de cuota excede el máximo permitido.");
        }

        // Asociar el estudiante a la cuota de pago
        cuotaPago.setEstudiante(estudiante);

        // Guardar la cuota de pago
        return cuotaPagoRepository.save(cuotaPago);
    }

    public void aplicarDescuentos(List<DescuentoEntity> descuentos) {
        // Obtener todas las cuotas pendientes
        List<CuotaPagoEntity> cuotasPendientes = cuotaPagoRepository.findByPagadaFalse();

        for (CuotaPagoEntity cuota : cuotasPendientes) {
            // Calcular el monto de la cuota con los nuevos descuentos correspondientes
            double montoCalculado = calcularMontoCuotaConDescuentos(cuota.getEstudiante());

            // Actualizar el monto de la cuota en la entidad
            cuota.setMonto(montoCalculado);

            // Guardar la cuota actualizada en la base de datos
            cuotaPagoRepository.save(cuota);
        }
    }

    private double calcularMontoCuotaConDescuentos(double montoBaseCuota, List<DescuentoEntity> descuentos) {
        double montoConDescuento = montoBaseCuota;

        for (DescuentoEntity descuento : descuentos) {
            if (cumpleCondicionesDescuento(montoBaseCuota, descuento)) {
                // Aplicar el descuento al monto de la cuota
                double porcentajeDescuento = descuento.getPorcentajeDescuento();
                montoConDescuento *= (1 - (porcentajeDescuento / 100));
            }
        }

        return montoConDescuento;
    }

    private boolean cumpleCondicionesDescuento(double montoBaseCuota, DescuentoEntity descuento) {
        // Obtén el umbral de monto del descuento
        double umbralMonto = descuento.getUmbralMonto();

        // Comprueba si el monto base de la cuota supera el umbral
        if (montoBaseCuota >= umbralMonto) {
            // Si el monto base es mayor o igual al umbral, el descuento se aplica
            return true;
        } else {
            // Si no, el descuento no se aplica
            return false;
        }
    }


}
