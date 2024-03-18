package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Tratamiento;

import java.util.List;

public interface ITratamientoService {

    void validar(String nombre, String descripcion, Double precio, Integer duracion) throws MiException;

    void saveTratamiento(String nombre, String descripcion, Double precio, Integer duracion) throws MiException;

    public List<Tratamiento> findTratamientos();

    public void deleteTratamiento(Long id);

    public void modificarTratamiento(Long id, Tratamiento tratamiento) throws MiException;

    public Tratamiento getTratamiento(Long id);

    public List<Tratamiento> getTratamientosById(List<Long> ids);

    public int getDuracionTratamientos(List<Long>ids);
}
