package com.frutimonchis.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "productos",
        indexes = {
                @Index(name = "ix_producto_nombre", columnList = "nombre"),
                @Index(name = "ix_producto_sku", columnList = "sku")
        },
        uniqueConstraints = {
                // Un mismo nombre no se puede repetir dentro de la misma categoría
                @UniqueConstraint(name = "uk_producto_nombre_categoria", columnNames = {"nombre", "categoria_id"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // Código interno opcional, único si lo usas
    @Column(length = 50, unique = true)
    private String sku;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnidadMedida unidadMedida;

    /**
     * Cantidad de unidades que trae la presentación (ej. CAJA de 12 = 12).
     * Se usa para convertir entradas/salidas por presentaciones a unidades.
     */
    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer unitMultiplier;

    // Precio neto por UNIDAD
    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal netPrice;

    // Stock expresado en UNIDADES
    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer minStock;

    @Column(length = 300)
    private String imageUrl;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "categoria_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_producto_categoria")
    )
    private CategoriaEntity categoria;

    @PrePersist
    public void prePersist() {
        if (activo == null) activo = true;
        if (unitMultiplier == null || unitMultiplier < 1) unitMultiplier = 1;
        if (stock == null) stock = 0;
        if (minStock == null) minStock = 0;
    }
}
