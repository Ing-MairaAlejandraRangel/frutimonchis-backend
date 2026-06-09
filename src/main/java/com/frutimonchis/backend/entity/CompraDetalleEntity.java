package com.frutimonchis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "compra_detalles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CompraDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Compra */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compra_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_compra_detalle_compra"))
    @JsonBackReference
    private CompraEntity compra;

    /** Producto comprado */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_compra_detalle_producto"))
    private ProductoEntity producto;

    /** Copia del nombre del producto al momento de comprar (histórico) */
    @Column(nullable = false, length = 120)
    private String nombreProducto;

    /** Unidad de medida histórica */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnidadMedida unidadMedida;

    /** Cantidad comprada (en UNIDADES) */
    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    /** Costo unitario en el momento de compra */
    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal costoUnitario;

    /** Subtotal = cantidad * costoUnitario */
    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal;
}
