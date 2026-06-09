package com.frutimonchis.backend.DTO;

import com.frutimonchis.backend.entity.UnidadMedida;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    // opcional, pero si lo envías podemos validarlo
    private String sku;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;

    @NotNull @Min(value = 1, message = "unitMultiplier debe ser >= 1")
    private Integer unitMultiplier;

    @NotNull @DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser >= 0.00")
    private BigDecimal netPrice;

    @NotNull @Min(value = 0, message = "El stock debe ser >= 0")
    private Integer stock;

    @NotNull @Min(value = 0, message = "El minStock debe ser >= 0")
    private Integer minStock;

    @Size(max = 300, message = "La URL de imagen es demasiado larga")
    private String imageUrl;

    // Si no lo envías, se asume true en @PrePersist
    private Boolean activo;

    @NotNull(message = "Debe indicar la categoría (id)")
    private Long categoriaId;
}
