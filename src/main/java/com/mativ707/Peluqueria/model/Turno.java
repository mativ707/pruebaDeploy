package com.mativ707.Peluqueria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaTurno;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean estado;
    private String dniCliente;

    @ElementCollection
    private List<Long> idTratamientos;
}
