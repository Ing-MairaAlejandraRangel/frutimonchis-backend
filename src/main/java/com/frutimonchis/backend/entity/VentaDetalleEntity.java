package com.frutimonchis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "venta_detalles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class VentaDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Venta */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venta_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_detalle_venta"))
    @JsonBackReference
    private VentaEntity venta;

    /** Producto vendido */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_detalle_producto"))
    private ProductoEntity producto;

    /** Copia del nombre del producto al momento de vender (histórico) */
    @Column(nullable = false, length = 120)
    private String nombreProducto;

    /** Unidad de medida usada (histórica) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnidadMedida unidadMedida;

    /** Cantidad vendida (en UNIDADES) */
    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    /** Precio unitario en el momento de la venta */
    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal precioUnitario;

    /** Subtotal = cantidad * precioUnitario */
    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal;
}
