package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.User;

public interface IUserService {
    public void validar(String nombre, String email, String password, String password2, String dni, String tel) throws MiException;

    public User getById(Long id);

    public User getUserByEmail(String email);

    public void crearUsuario(String nombre, String email, String password, String password2, String dni, String tel) throws MiException;

    public void eliminarUsuario(String email) throws MiException;
}
