package com.frutimonchis.backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovimientoAjusteDTO {

    @NotNull(message = "Debe indicar el producto")
    private Long productoId;

    /** Cantidad positiva para sumar stock, negativa para restar */
    @NotNull
    private Integer cantidad; // puede ser negativa

    /** Valor unitario referencial (opcional) */
    private BigDecimal valorUnitario;

    private String observaciones;
}
