package com.mativ707.Peluqueria.controller;

import com.mativ707.Peluqueria.model.Cliente;
import com.mativ707.Peluqueria.model.Turno;
import com.mativ707.Peluqueria.service.IClienteService;
import com.mativ707.Peluqueria.service.ITratamientoService;
import com.mativ707.Peluqueria.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/") //localhost:8080
public class PortalController {

    @Autowired
    private ITurnoService turnoServ;
    @Autowired
    private IClienteService clientServ;
    @Autowired
    private ITratamientoService tratServ;

    @PreAuthorize("permitAll")
    @GetMapping("/")
    public String index(ModelMap modelo,
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String exito,
                        RedirectAttributes redirectAttributes) {

        if (error != null) {
            modelo.put("error", "El usuario o contraseña son inválidos");
        }
        if (exito != null) {
            modelo.addAttribute("exito", exito);
            redirectAttributes.getFlashAttributes().clear();
        }
        return "index.html";
    }

    //AuthenticationPrincipal es para lograr obtener los datos del usuario logueado
    //el import de USER debe ser del paquete org.springframework.config.core.userdetails.User
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo,
                         @AuthenticationPrincipal UserDetails logueado,
                         @RequestParam(name = "error", required = false) String error,
                         @RequestParam(name = "exito", required = false) String exito,
                         RedirectAttributes redirectAttributes) {

        if (error != null && !error.isEmpty()) {
            modelo.addAttribute("error", error);
            redirectAttributes.getFlashAttributes().clear();
        } else if (exito != null && !exito.isEmpty()) {
            modelo.addAttribute("exito", exito);
            redirectAttributes.getFlashAttributes().clear();
        }

        Optional<Cliente> cliente = clientServ.getClienteByEmail(logueado.getUsername());
        if (cliente.isPresent()) {
            String email = cliente.get().getEmail();
            Turno turno = cliente.get().getTurno();

            modelo.put("turnoPendiente", turno);
        }

        modelo.put("fecha", LocalDate.now());
        modelo.put("tratamientos", tratServ.findTratamientos());

        return "inicio.html";
    }

}
