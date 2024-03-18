package com.mativ707.Peluqueria.controller;

import com.mativ707.Peluqueria.dto.CrearClienteDTO;
import com.mativ707.Peluqueria.service.IClienteService;
import com.mativ707.Peluqueria.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userServ;
    @Autowired
    private IClienteService clientServ;

    @GetMapping("/registro")
    public String registrar(ModelMap modelo) {
        modelo.addAttribute("clienteDTO", new CrearClienteDTO());
        return "registro.html";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@ModelAttribute("clienteDTO") CrearClienteDTO clienteDTO,
                                   ModelMap modelo,
                                   RedirectAttributes redirectAttributes) {
        try {
            clientServ.crearCliente(clienteDTO.getNombre(), clienteDTO.getEmail(), clienteDTO.getPassword(),
                    clienteDTO.getPassword2(), clienteDTO.getDni(), clienteDTO.getTel());

            redirectAttributes.addFlashAttribute("exito", "Se ha registrado con éxito!");
            return "redirect:/";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "registro.html";
        }
    }

}
