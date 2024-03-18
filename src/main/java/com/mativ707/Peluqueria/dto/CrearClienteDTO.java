package com.mativ707.Peluqueria.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearClienteDTO {

    private String nombre;
    private String email;
    private String password;
    private String password2;
    private String dni;
    private String tel;
}
