package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {

    public void validar(String nombre, String email, String password, String password2, String dni, String tel) throws MiException;

    public Cliente getClienteById(Long id);

    public Optional<Cliente> getClienteByEmail(String email);

    public Cliente getByDni(String dniCliente);

    public void crearCliente(String nombre, String email, String password, String password2, String dni, String tel) throws MiException;

    public void deleteCliente(Long id);

    public List<Cliente> getClientes();
}
