package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.enumeraciones.Rol;
import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Cliente;
import com.mativ707.Peluqueria.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ClienteService implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepo;

    @Override
    public void validar(String nombre, String email, String password, String password2, String dni, String tel) throws MiException {

        List<String> listaEmails = clienteRepo.getAllEmails();
        List<String> listaDni = clienteRepo.getAllDni();

        if (nombre == null || nombre.isEmpty())
            throw new MiException("El nombre no puede ser null o estar vacío");

        if (email == null || email.isEmpty())
            throw new MiException("El email no puede ser null o estar vacío");
        if (listaEmails.contains(email))
            throw new MiException("El email ya se encuentra asociado a una cuenta existente");

        if (password == null || password.isEmpty())
            throw new MiException("El password no puede ser null o estar vacío");

        if (!password2.equals(password))
            throw new MiException("Las password no coinciden");

        if (dni == null || dni.isEmpty())
            throw new MiException("El dni no es válido");
        if (listaDni.contains(dni))
            throw new MiException("El dni ya se encuentra asociado a una cuenta");

        if (tel == null || tel.isEmpty())
            throw new MiException("El teléfono ingresado no es válido");
    }

    @Override
    public Cliente getClienteById(Long id) {
        return clienteRepo.findById(id).orElse(null);
    }

    @Override
    public Optional<Cliente> getClienteByEmail(String email) {
        return clienteRepo.findByEmail(email);
    }

    @Override
    public Cliente getByDni(String dniCliente) {
        return clienteRepo.buscarPorDni(dniCliente);
    }

    @Override
    public void crearCliente(String nombre, String email, String password, String password2, String dni, String tel)
                throws MiException {

        try {
            validar(nombre, email, password, password2, dni, tel);
            Cliente cliente = new Cliente();
            cliente.setRol(Rol.USER);

            cliente.setEmail(email);
            cliente.setNombre(nombre);
            cliente.setPassword(new BCryptPasswordEncoder().encode(password));
            cliente.setDni(dni);
            cliente.setTel(tel);
            clienteRepo.save(cliente);

        } catch (Exception e) {
            throw new MiException("Ocurrió un error al intentar crear cliente. ERROR: " + e.getMessage());
        }
    }

    @Override
    public void deleteCliente(Long id) {
        clienteRepo.deleteById(id);
    }

    @Override
    public List<Cliente> getClientes() {
        return clienteRepo.findAll();
    }

}
