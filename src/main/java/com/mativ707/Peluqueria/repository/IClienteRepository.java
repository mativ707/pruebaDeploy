package com.mativ707.Peluqueria.repository;

import com.mativ707.Peluqueria.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    @Query("SELECT cl FROM Cliente cl " +
            "WHERE cl.dni = :dniCliente")
    Cliente buscarPorDni(@Param("dniCliente") String dniCliente);

    @Query("SELECT cl.email FROM Cliente cl")
    List<String> getAllEmails();

    @Query("SELECT cl.dni FROM Cliente cl")
    List<String> getAllDni();
}
