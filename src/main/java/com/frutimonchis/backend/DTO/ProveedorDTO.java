package com.frutimonchis.backend.DTO;

import com.frutimonchis.backend.entity.TipoProveedor;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProveedorDTO {

    private Long id;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    private String nit;

    @NotNull(message = "Debe indicar el tipo de proveedor")
    private TipoProveedor tipo;

    @Email(message = "Correo electrónico inválido")
    private String email;

    private String telefono;

    private String direccion;

    private String ciudad;

    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    private BigDecimal saldoPendiente;

    private Boolean activo;
}
