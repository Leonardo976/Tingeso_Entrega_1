package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.SubirDataEntity;
import edu.mtisw.monolithicwebapp.repositories.SubirDataRepository;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubirDataService {
    @Autowired
    private SubirDataRepository dataRepository;

    private final Logger logg = LoggerFactory.getLogger(SubirDataService.class);

    public ArrayList<SubirDataEntity> obtenerData() {
        return (ArrayList<SubirDataEntity>) dataRepository.findAll();
    }

    @Generated
    public String guardar(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            if ((!file.isEmpty()) && (filename.toUpperCase().equals("DATA.TXT"))) {
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                } catch (IOException e) {
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con éxito!";
        } else {
            return "No se pudo guardar el archivo";
        }
    }

    @Generated
    public void leerTxt(String direccion) {
        String texto = "";
        BufferedReader bf = null;
        dataRepository.deleteAll();
        try {
            bf = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(direccion))));
            String temp = "";
            String bfRead;
            while ((bfRead = bf.readLine()) != null) {
                procesarLineaCSV(bfRead);
                temp = temp + "\n" + bfRead;
            }
            texto = temp;
            System.out.println("Archivo leído exitosamente");
        } catch (Exception e) {
            System.err.println("No se encontró el archivo");
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    logg.error("ERROR", e);
                }
            }
        }
    }

    private void procesarLineaCSV(String lineaCSV) {
        String[] campos = lineaCSV.split(";");
        if (campos.length == 3) { // Asegúrate de que haya tres campos
            String rut = campos[0];
            String fechaExamen = campos[1];
            Integer puntajeObtenido = Integer.parseInt(campos[2]);

            SubirDataEntity newData = new SubirDataEntity();
            newData.setRut(rut);
            newData.setFechaExamen(fechaExamen);
            newData.setPuntajeObtenido(puntajeObtenido);

            guardarData(newData);
        } else {
            logg.warn("Línea CSV incorrecta: " + lineaCSV);
        }
    }

    public void guardarData(SubirDataEntity data) {
        dataRepository.save(data);
    }

    public void guardarDataDB(String fecha, String rut) {
        SubirDataEntity newData = new SubirDataEntity();
        newData.setFechaExamen(fecha);
        newData.setRut(rut);
        guardarData(newData);
    }

    public Optional<SubirDataEntity> obtenerEspecifico(String rut, String fecha) {
        return dataRepository.findByRutAndFechaExamen(rut, fecha);
    }

    public List<String> obtenerRuts() {
        return dataRepository.findDistinctRut();
    }

    public String obtenerFechaRut(String rut) {
        return dataRepository.findFechaExamenByRut(rut);
    }

    public void insertarData(String rut, String fechaInicial) throws ParseException {
        Calendar calendario = prepararCalendario(fechaInicial);
        int lastDay = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= lastDay; day++) {
            calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), day);
            if (!(comprobarFinesSemana(calendario))) {
                String fecha_real = formatDate(calendario);
                SubirDataEntity data = new SubirDataEntity();
                data.setRut(rut);
                data.setFechaExamen(fecha_real);
                guardarData(data);
            }
        }
    }

    public Calendar prepararCalendario(String fecha) throws ParseException {
        Calendar calendario = Calendar.getInstance();
        DateFormat date1 = new SimpleDateFormat("yyyy/MM/dd");
        Date real_fecha = date1.parse(fecha);
        calendario.setTime(real_fecha);
        return calendario;
    }

    public Boolean comprobarFinesSemana(Calendar calendario) {
        int dia = calendario.get(Calendar.DAY_OF_WEEK);
        return dia == Calendar.SATURDAY || dia == Calendar.SUNDAY;
    }

    public String formatDate(Calendar calendario) {
        DateFormat date1 = new SimpleDateFormat("yyyy/MM/dd");
        return date1.format(calendario.getTime());
    }

    public void eliminarData(ArrayList<SubirDataEntity> datas) {
        dataRepository.deleteAll(datas);
    }

    public ArrayList<SubirDataEntity> obtenerData(String rut) {
        return dataRepository.deleteByRut(rut);
    }
}
