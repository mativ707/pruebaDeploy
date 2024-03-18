package com.mativ707.Peluqueria.controller;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Cliente;
import com.mativ707.Peluqueria.model.Tratamiento;
import com.mativ707.Peluqueria.model.Turno;
import com.mativ707.Peluqueria.model.User;
import com.mativ707.Peluqueria.service.IClienteService;
import com.mativ707.Peluqueria.service.ITratamientoService;
import com.mativ707.Peluqueria.service.ITurnoService;
import com.mativ707.Peluqueria.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/turnos")
@PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
public class TurnoController {

    @Autowired
    private ITurnoService turnoServ;
    @Autowired
    private ITratamientoService tratServ;
    @Autowired
    private IClienteService clientServ;
    @Autowired
    private IUserService userServ;

    @GetMapping("/getTurnos")
    public String getTurnos(HttpSession session, ModelMap modelo) {

        Object usuarioEnSesion = session.getAttribute("usuarioSession");

        if (usuarioEnSesion instanceof Cliente) {
            Cliente cliente = (Cliente) usuarioEnSesion;
            List<Turno> turnos = turnoServ.getTurnoByDniCliente(cliente.getDni());
            modelo.put("turnos", turnos);

        } else if (usuarioEnSesion instanceof User) {
            User user = (User) usuarioEnSesion;
            List<Turno> turnos = turnoServ.getTurnoByDniCliente(user.getDni());
            modelo.put("turnos", turnos);
        }

        return "turnos.html";
    }

    @PostMapping("/comprobarDisponibilidad")
    public String comprobarDisponibilidad(@AuthenticationPrincipal org.springframework.security.core.userdetails.User logueado,
                                          ModelMap modelo,
                                          @RequestParam(required = false) LocalDate fechaTurno,
                                          @RequestParam(required = false) List<Long> idDuracionTratamientos,
                                          RedirectAttributes redirectAttributes) {


        try {

            Optional<Cliente> cliente = clientServ.getClienteByEmail(logueado.getUsername());
            if (cliente.isPresent()) {
                String dni = cliente.get().getDni();
                modelo.addAttribute("dniUsuario", dni);
            }

            List<LocalTime> horas = turnoServ.getHorasDisponiblesPorFechaHORA(fechaTurno, idDuracionTratamientos);
            modelo.put("horasDisponibles", horas);
            System.out.println(horas);

            modelo.put("fechaEscogida", fechaTurno);
            System.out.println(fechaTurno);

            List<Tratamiento> tratamientosEscogidos = tratServ.getTratamientosById(idDuracionTratamientos);
            modelo.put("tratamientosEscogidos", tratamientosEscogidos);

            return "registrarTurno.html";

        } catch (MiException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inicio";
        }
    }


    @PostMapping("/registroTurno")
    public String registroTurno(@RequestParam String dniUsuario,
                                @RequestParam List<Long> tratamientosEscogidos,
                                @RequestParam LocalDate fechaEscogida,
                                @RequestParam LocalTime horaInicio,
                                RedirectAttributes redirectAttributes) throws MiException {

        try {
            turnoServ.saveTurno(dniUsuario, tratamientosEscogidos, fechaEscogida, horaInicio);
            redirectAttributes.addFlashAttribute("exito", "Se creó el turno con éxito");
            return "redirect:/inicio";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inicio";
        }
    }

}
