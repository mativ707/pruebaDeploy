package com.mativ707.Peluqueria.controller;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Cliente;
import com.mativ707.Peluqueria.model.Tratamiento;
import com.mativ707.Peluqueria.model.Turno;
import com.mativ707.Peluqueria.service.IClienteService;
import com.mativ707.Peluqueria.service.ITratamientoService;
import com.mativ707.Peluqueria.service.ITurnoService;
import com.mativ707.Peluqueria.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private IUserService userServ;
    @Autowired
    private IClienteService clientServ;
    @Autowired
    private ITratamientoService tratServ;
    @Autowired
    private ITurnoService turnoServ;


    //CLIENTES / USUARIOS
    @GetMapping("/clientes")
    public String listaClientes(ModelMap modelo,
                                @RequestParam(required = false)String error,
                                @RequestParam(required = false)String exito,
                                RedirectAttributes redirectAttributes) {

        List<Cliente> clientes = clientServ.getClientes();
        if(error != null){
            modelo.addAttribute("error", error);
            redirectAttributes.getFlashAttributes().clear();
        }
        if(exito != null){
            modelo.addAttribute("exito", exito);
            redirectAttributes.getFlashAttributes().clear();
        }

        modelo.put("clientes", clientes);
        return "usuarios.html";
    }

    @PostMapping("/deleteUser/{idUsuario}")
    public String deleteUser(@PathVariable Long idUsuario,
                             RedirectAttributes redirectAttributes,
                             ModelMap modelo) throws MiException {
        try {
            clientServ.deleteCliente(idUsuario);
            redirectAttributes.addFlashAttribute("exito",
                    "Se ha eliminado con éxito el cliente de la BD");
        } catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/clientes";
    }


    //TRATAMIENTOS
    @GetMapping("/getTratamientos")
    public String verTratamientos(Tratamiento tratamiento,
                                  ModelMap modelo,
                                  @RequestParam(required = false)String error,
                                  @RequestParam(required = false)String exito,
                                  RedirectAttributes redirectAttributes) {

        List<Tratamiento> tratamientos = tratServ.findTratamientos();
        modelo.addAttribute("tratamientos", tratamientos);
        if(exito != null){
            modelo.addAttribute("exito","Se ha creado el tratamiento con éxito");
            redirectAttributes.getFlashAttributes().clear();
        }
        if(error != null){
            modelo.addAttribute("error", error);
            redirectAttributes.getFlashAttributes().clear();
        }

        return "tratamientos.html";
    }

    @PostMapping("/saveTratamiento")
    public String saveTratamiento(@ModelAttribute("tratamiento") Tratamiento tratamiento,
                                  ModelMap modelo,
                                  RedirectAttributes redirectAttributes) throws MiException {

        try {
            tratServ.saveTratamiento(tratamiento.getNombre(),
                    tratamiento.getDescripcion(),
                    tratamiento.getPrecio(),
                    tratamiento.getDuracion());
            //Mensaje de éxito
            redirectAttributes.addFlashAttribute("exito", "Se ha guardado el tratamiento con éxito!");
            return "redirect:/admin/getTratamientos";
        } catch (Exception e) {
            //Mensaje de error
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/getTratamientos";
        }
    }

    @PostMapping("/deleteTratamiento/{idTratamiento}")
    public String deleteTratamiento(@PathVariable Long idTratamiento,
                                    ModelMap modelo,
                                    RedirectAttributes redirectAttributes) {
        try {
            modelo.put("tratamiento", tratServ.getTratamiento(idTratamiento));
            tratServ.deleteTratamiento(idTratamiento);
            redirectAttributes.addFlashAttribute("exito", "Se ha eliminado el tratamiento con éxito");
            return "redirect:/admin/getTratamientos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/getTratamientos";
        }

    }

    @GetMapping("/editarTrat/{id}")
    public String editarTratamientoEntidad(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("tratamientoModificar", tratServ.getTratamiento(id));
        return "modificarTratamiento.html";
    }

    @PostMapping("/editarTratamiento/{id}")
    public String editarTratamiento(@PathVariable Long id, @RequestParam String nombre,
                                    @RequestParam String descripcion, @RequestParam Double precio,
                                    @RequestParam Integer duracion, ModelMap modelo) throws MiException {

        try {
            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setNombre(nombre);
            tratamiento.setPrecio(precio);
            tratamiento.setDescripcion(descripcion);
            tratamiento.setDuracion(duracion);

            tratServ.modificarTratamiento(id, tratamiento);
            modelo.put("exito", "Se ha modificado con éxito!");

            return "redirect:/admin/getTratamientos";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            System.out.println(e.getMessage());
            return "tratamientos.html";
        }

    }

    //TURNOS
    @GetMapping("/getTurnos")
    public String getTurnos(@RequestParam(required = false)String error,
                            @RequestParam(required = false)String exito,
                            ModelMap modelo,
                            RedirectAttributes redirectAttributes)
                            throws MiException {

        List<Turno> turnos = turnoServ.getTurnos();
        modelo.put("turnos", turnos);

        if(error != null){
            modelo.addAttribute("error" , error);
            redirectAttributes.getFlashAttributes().clear();
        }
        if(exito != null){
            modelo.addAttribute("exito", exito);
            redirectAttributes.getFlashAttributes().clear();
        }
        return "turnos.html";
    }


    @PostMapping("/cerrarTurno/{id}")
    public String editarEstado(@PathVariable Long id) throws MiException    {
        turnoServ.cerrarTurno(id);
        return "redirect:/admin/getTurnos";
    }

    @PostMapping("/eliminarTurno/{id}")
    public String deleteTurno(@PathVariable Long id,
                              ModelMap modelo,
                              RedirectAttributes redirectAttributes) throws MiException {
        try{
            turnoServ.deleteTurno(id);
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/getTurnos";
        }
        return "redirect:/admin/getTurnos";
    }
}

