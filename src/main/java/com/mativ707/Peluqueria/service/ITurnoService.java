package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITurnoService {

    public List<Turno>getTurnoByDniCliente(String dniCliente);

    public void validar(String dniCliente, List<Long> idTratamientos, LocalDate fechaTurno) throws MiException;

    public Turno getTurnoActivo(String dniCliente);

    public void saveTurno(String dniCliente, List<Long> idTratamientos, LocalDate fechaTurno, LocalTime horaInicio) throws MiException;

    public void deleteTurno(Long id) throws MiException;

    public void modificarTurno(Long id, Turno turno);

    public void cerrarTurno(Long id) throws MiException;

    public List<LocalTime> getHorasDisponiblesPorFechaHORA(LocalDate fecha, List<Long> idDuracionTratamientos) throws MiException;

    public List<Turno> getTurnos();

}
