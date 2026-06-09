package com.frutimonchis.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VentaItemDTO {

    @NotNull(message = "Debe indicar el producto")
    private Long productoId;

    @NotNull @Min(value = 1, message = "La cantidad debe ser >= 1")
    private Integer cantidad;

    /** Opcional: si no lo envías se usará el netPrice del producto */
    private BigDecimal precioUnitario;
}
