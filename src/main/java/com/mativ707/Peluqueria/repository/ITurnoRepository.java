package com.mativ707.Peluqueria.repository;

import com.mativ707.Peluqueria.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    @Query("SELECT t FROM Turno t " +
            "WHERE t.dniCliente = :dniCliente")
    List<Turno> getTurnosByDniCliente(String dniCliente);

    @Query("SELECT t FROM Turno t " +
            "WHERE t.dniCliente = :dniCliente " +
            "AND t.estado = TRUE")
    Turno getTurnoActivoByDniCliente(String dniCliente);

    @Query("SELECT t FROM Turno t " +
            "WHERE t.fechaTurno = :fechaTurno " +
            "AND t.estado = TRUE")
    List<Turno> getTurnosActivosPorDia(@Param("fechaTurno") LocalDate fechaTurno);

//    @Query("SELECT t FROM Turno t WHERE :resto ")
//    public List<Turno> getByFilters(@Param("resto") String query);
}
