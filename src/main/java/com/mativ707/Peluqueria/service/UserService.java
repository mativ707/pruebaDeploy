package com.mativ707.Peluqueria.service;

import com.mativ707.Peluqueria.enumeraciones.Rol;
import com.mativ707.Peluqueria.excepciones.MiException;
import com.mativ707.Peluqueria.model.User;
import com.mativ707.Peluqueria.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepo;

    @Override
    public void validar(String nombre, String email, String password, String password2, String dni, String tel) throws MiException {
        if(nombre == null || nombre.isEmpty()){
            throw new MiException("El nombre no puede ser null o estar vacío");
        }
        if(email == null || email.isEmpty()){
            throw new MiException("El email no puede ser null o estar vacío");
        }
        if(password == null || password.isEmpty()){
            throw new MiException("El password no puede ser null o estar vacío");
        }
        if(!password2.equals(password)){
            throw new MiException("Las password no coinciden");
        }
        if(dni == null || dni.isEmpty()){
            throw new MiException("El dni no es válido");
        }
        if(tel == null || tel.isEmpty()){
            throw new MiException("El teléfono ingresado no es válido");
        }
    }

    @Override
    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.buscarPorEmail(email);
    }

    @Override
    public void crearUsuario(String nombre, String email, String password, String password2, String dni, String tel) throws MiException {

        validar(nombre,email,password,password2, dni, tel);
        try{
            User user = new User();
            user.setRol(Rol.USER);

            user.setEmail(email);
            user.setNombre(nombre);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setDni(dni);
            user.setTel(tel);
            userRepo.save(user);
            System.out.println("Se guardó el usuario");
        }catch(Exception e){
            throw new MiException("Ocurrió un error al intentar crear cliente. ERROR: " + e.getMessage());
        }
    }

    @Override
    public void eliminarUsuario(String email) throws MiException {
        try {
            User userToDelete = userRepo.buscarPorEmail(email);
            userRepo.deleteById(userToDelete.getId());
        } catch (Exception e) {
            throw new MiException("Hubo un inconveniente al intentar eliminar el usuario" +
                    "Error: " + e.getMessage());
        }
    }

}

