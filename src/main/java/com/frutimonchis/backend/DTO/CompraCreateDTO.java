package com.frutimonchis.backend.DTO;

import com.frutimonchis.backend.entity.MetodoPago;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CompraCreateDTO {

    @NotNull(message = "Debe indicar el proveedor")
    private Long proveedorId;

    @NotNull(message = "Debe indicar el método de pago")
    private MetodoPago metodoPago;

    /** Monto pagado al momento (0 si todo queda pendiente) */
    @NotNull @DecimalMin(value = "0.00", inclusive = true, message = "El pago debe ser >= 0")
    private BigDecimal totalPagado;

    private String observaciones;

    @NotNull @Size(min = 1, message = "Debe incluir al menos un ítem")
    private List<CompraItemDTO> items;
}
