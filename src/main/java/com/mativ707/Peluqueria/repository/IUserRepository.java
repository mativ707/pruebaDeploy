package com.mativ707.Peluqueria.repository;

import com.mativ707.Peluqueria.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "WHERE u.email = :email")
    User buscarPorEmail(@Param("email")String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.dni = :dni")
    User buscarPorDni(@Param("dni") String dni);

}