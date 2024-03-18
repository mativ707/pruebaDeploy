package com.mativ707.Peluqueria.config;

import com.mativ707.Peluqueria.model.Cliente;
import com.mativ707.Peluqueria.model.User;
import com.mativ707.Peluqueria.repository.IClienteRepository;
import com.mativ707.Peluqueria.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private IClienteRepository clienteRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.buscarPorEmail(email);

        if (user != null) {

            List<GrantedAuthority> permisos = new ArrayList<GrantedAuthority>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuarioSession", user);

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), permisos);

        } else {

            Optional<Cliente> cliente = clienteRepo.findByEmail(email);

            if (cliente.isPresent()){
                List<GrantedAuthority> permisos = new ArrayList<GrantedAuthority>();

                GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + cliente.get().getRol().toString());

                permisos.add(p);

                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

                HttpSession session = attr.getRequest().getSession(true);

                session.setAttribute("usuarioSession", cliente);

                return new org.springframework.security.core.userdetails.User(cliente.get().getEmail(), cliente.get().getPassword(), permisos);
            }
        }
        throw new UsernameNotFoundException("Cliente no encontrado: " + email);
    }
}
