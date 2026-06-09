package com.frutimonchis.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompraItemDTO {

    @NotNull(message = "Debe indicar el producto")
    private Long productoId;

    @NotNull @Min(value = 1, message = "La cantidad debe ser >= 1")
    private Integer cantidad;

    @NotNull(message = "Debe indicar el costo unitario")
    private BigDecimal costoUnitario;
}
