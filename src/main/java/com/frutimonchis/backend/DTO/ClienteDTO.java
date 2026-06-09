package com.frutimonchis.backend.DTO;

import com.frutimonchis.backend.entity.TipoCliente;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "El nombre o razón social es obligatorio")
    private String nombre;

    /** Puede ser CC/NIT; opcional pero si viene se valida unicidad en servicio */
    private String documento;

    @NotNull(message = "Debe indicar el tipo de cliente")
    private TipoCliente tipo;

    @Email(message = "Correo inválido")
    private String email;

    /** Libre; si quieres forzar formato, podemos poner un regex más adelante */
    private String telefono;

    private String direccion;

    private String ciudad;

    /** si no envían, se asume 0.00 en el entity */
    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    private Boolean activo;
}
