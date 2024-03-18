package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Tratamiento;
import com.mativ707.Peluqueria.repository.ITratamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TratamientoService implements ITratamientoService {

    @Autowired
    private ITratamientoRepository tratRepo;

    @Override
    public void validar(String nombre, String descripcion, Double precio, Integer duracion) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre del tratamiento no es válido");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiException("La descripcion del tratamiento no es válida");
        }
        if (precio == null || precio.isNaN()) {
            throw new MiException("El precio del tratamiento no es válido");
        }
        if(duracion == null || duracion == 0){
            throw new MiException("La duracion no es válida");
        }
    }

    @Override
    public void saveTratamiento(String nombre, String descripcion, Double precio, Integer duracion) throws MiException {

        validar(nombre, descripcion, precio, duracion);

        try {
            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setNombre(nombre);
            tratamiento.setDescripcion(descripcion);
            tratamiento.setPrecio(precio);
            tratamiento.setDuracion(duracion);

            tratRepo.save(tratamiento);

        } catch (Exception e) {
            throw new MiException("Ocurrió un error al intentar crear tratamiento. ERROR: " + e.getMessage());
        }
    }

    @Override
    public List<Tratamiento> findTratamientos() {
        return tratRepo.findAll();
    }

    @Override
    public void deleteTratamiento(Long id) {
        tratRepo.deleteById(id);
    }

    @Override
    public void modificarTratamiento(Long id, Tratamiento tratamiento) throws MiException {

        Optional<Tratamiento> response = tratRepo.findById(id);

        if(response.isPresent()){
            Tratamiento tratamientoModificar = response.get();
            tratamientoModificar.setNombre(tratamiento.getNombre());
            tratamientoModificar.setDescripcion(tratamiento.getDescripcion());
            tratamientoModificar.setPrecio(tratamiento.getPrecio());
            tratamientoModificar.setDuracion(tratamiento.getDuracion());

            tratRepo.save(tratamientoModificar);
        } else{
            throw new MiException("No se ha encontrado el tratamiento a modificar");
        }
    }

    @Override
    public Tratamiento getTratamiento(Long id) {
        return tratRepo.findById(id).orElse(null);
    }

    @Override
    public List<Tratamiento> getTratamientosById(List<Long> ids) {
        return tratRepo.findAllById(ids);
    }

    @Override
    public int getDuracionTratamientos(List<Long>ids){
        List<Tratamiento> tratamientos = tratRepo.findAllById(ids);
        int duracionTratamientos = 0;

        //Recorrer los valores y sumarlos en la variable de duracion total del turno
        for (Tratamiento tratamiento : tratamientos) {
            duracionTratamientos += tratamiento.getDuracion();
        }

        return duracionTratamientos;
    }
}

