package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Cliente;
import com.mativ707.Peluqueria.model.Turno;
import com.mativ707.Peluqueria.repository.IClienteRepository;
import com.mativ707.Peluqueria.repository.ITurnoRepository;
import com.mativ707.Peluqueria.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private ITurnoRepository turnoRepo;
    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private ITratamientoService tratServ;
    @Autowired
    private IClienteService clientServ;
    @Autowired
    private IClienteRepository clientRepo;

    @Override
    public void validar(String dniCliente, List<Long> idTratamientos, LocalDate fechaTurno) throws MiException {

        if (dniCliente == null || dniCliente.isEmpty()) {
            throw new MiException("El dni no puede ser nulo");
        }
        if (idTratamientos == null || idTratamientos.isEmpty()) {
            throw new MiException("No se ha seleccionado ningun tratamiento");
        }

        if (fechaTurno == null || fechaTurno.isBefore(LocalDate.now())) {
            throw new MiException("La fecha seleccionada no es válida");
        }

    }

    @Override
    public List<LocalTime> getHorasDisponiblesPorFechaHORA(LocalDate fecha, List<Long> idDuracionTratamientos) throws MiException {

        // PRIMERO DEFINIMOS HORARIO LABORAL
        LocalTime[] horas = {
                LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
                LocalTime.of(11, 0), LocalTime.of(12, 0), LocalTime.of(15, 0),
                LocalTime.of(16, 0), LocalTime.of(17, 0), LocalTime.of(18, 0),
                LocalTime.of(19, 0), LocalTime.of(20, 0), LocalTime.of(21, 0)};

        List<LocalTime> horasDisponibles = new ArrayList<>(Arrays.asList(horas));

        // DEBUG
        System.out.println("HORAS AL INICIO: " + horasDisponibles);

        try {

            if (fecha == null || fecha.isBefore(LocalDate.now())) {
                throw new MiException("La fecha no es válida");
            }
            if (idDuracionTratamientos == null || idDuracionTratamientos.isEmpty()) {
                throw new MiException("Ocurrió un error con los tratamientos seleccionados");
            }

            System.out.println("Id's tratamientos recibidos: " + idDuracionTratamientos.size());
            System.out.println("\n");

            // Lista de turnos obtenida mediante repo por fecha
            List<Turno> turnos = turnoRepo.getTurnosActivosPorDia(fecha);
            // Obtenemos la duración total de los tratamientos del turno a registrar
            int duracionTratamientos = tratServ.getDuracionTratamientos(idDuracionTratamientos);

            if (!turnos.isEmpty()) {
                // Se recorre la lista de turnos por la fecha elegida
                for (Turno aux : turnos) {
                    horasDisponibles.remove(aux.getHoraInicio());
                    System.out.println("se removió hora inicial");

                    if (aux.getHoraFin().isAfter(aux.getHoraInicio().plusHours(1))) {
                        // Se eliminan las horas de por medio de los turnos que superan la duración de una hora
                        for (LocalTime hora = aux.getHoraInicio().plusHours(1); hora.isBefore(aux.getHoraFin()); hora = hora.plusHours(1)) {
                            horasDisponibles.remove(hora);
                        }
                    }
                }
            }

            // DEBUG
            System.out.println("HORAS NUEVAS: " + horasDisponibles);

            // Comprobamos las horas que puedan registrar un turno de más de una hora y de la duración total de los tratamientos
            for (int i = 0; i < horasDisponibles.size() - 1; i++) {
                LocalTime horaInicial = horasDisponibles.get(i);
                LocalTime horaSiguiente = horasDisponibles.get(i + 1);

                for (int j = 0; j < duracionTratamientos - 1; j++) {
                    if (horaSiguiente.minusHours(j).isAfter(horaInicial)) {
                        horasDisponibles.remove(horaInicial);
                        break;
                    }
                }
            }

        } catch (MiException e) {
            throw new MiException("Ocurrió un error en comprobar. ERROR: " + e.getMessage());
        }

        // DEBUG
        System.out.println("HORAS FINALES: " + horasDisponibles);
        return horasDisponibles;
    }

    @Override
    public List<Turno> getTurnos() {
        return turnoRepo.findAll();
    }

    @Override
    public List<Turno> getTurnoByDniCliente(String dniCliente) {
        return turnoRepo.getTurnosByDniCliente(dniCliente);
    }

    @Override
    public Turno getTurnoActivo(String dniCliente) {
        return turnoRepo.getTurnoActivoByDniCliente(dniCliente);
    }

    @Override
    public void saveTurno(String dniCliente,
                          List<Long> idTratamientos,
                          LocalDate fechaTurno,
                          LocalTime horaInicio) throws MiException {

        try {
            validar(dniCliente, idTratamientos, fechaTurno);
            //Obtenemos el cliente para verificar que no posea algun turno asociado
            Cliente cliente = clientServ.getByDni(dniCliente);

            if (cliente.getTurno() != null)
                throw new MiException("El cliente ya posee un turno asociado");

            //Instanciamos un nuevo turno
            Turno turno = new Turno();
            //Se habilita el turno
            turno.setEstado(true);

            turno.setDniCliente(dniCliente);
            turno.setIdTratamientos(idTratamientos);
            int duracion = tratServ.getDuracionTratamientos(idTratamientos);
            turno.setFechaTurno(fechaTurno);
            turno.setHoraInicio(horaInicio);
            turno.setHoraFin(horaInicio.plusHours(duracion));

            //Guardamos
            turnoRepo.save(turno);
            //Asignamos el turno al cliente
            cliente.setTurno(turno);
            clientRepo.save(cliente);

        } catch (Exception e) {
            throw new MiException("Ocurrió un error al crear el turno. ERROR: " + e.getMessage());
        }
    }

    @Override
    public void deleteTurno(Long id) throws MiException {

        Optional<Turno> turnoEliminar = turnoRepo.findById(id);
        if (!turnoEliminar.isPresent())
            throw new MiException("El turno no se ha encontrado");

        Cliente cliente = clientServ.getByDni(turnoEliminar.get().getDniCliente());
        cliente.setTurno(null);
        clientRepo.save(cliente);
        turnoRepo.deleteById(id);
    }

    @Override
    public void modificarTurno(Long id, Turno turno) {
        System.out.println("Funcion no implementada aun");
    }

    @Override
    public void cerrarTurno(Long id) throws MiException {
        try {
            Turno turnoACerrar = turnoRepo.findById(id).orElse(null);

            if (turnoACerrar == null)
                throw new MiException("El turno no se ha encontrado");

            if (turnoACerrar.getEstado()) {
                turnoACerrar.setEstado(false);
                Cliente cliente = clientServ.getByDni(turnoACerrar.getDniCliente());
                cliente.setTurno(null);
                clientRepo.save(cliente);
                //Guardamos el turno cerrado
                turnoRepo.save(turnoACerrar);
            }

        } catch (Exception e) {
            throw new MiException("Ocurrio un error al intentar cerrar el turno. ERROR: " + e.getMessage());
        }
    }
}

