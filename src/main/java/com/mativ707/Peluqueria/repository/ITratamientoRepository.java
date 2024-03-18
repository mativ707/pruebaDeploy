package com.mativ707.Peluqueria.repository;

import com.mativ707.Peluqueria.model.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITratamientoRepository extends JpaRepository<Tratamiento, Long> {
}
