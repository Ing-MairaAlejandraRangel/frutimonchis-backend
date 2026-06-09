package com.frutimonchis.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario", indexes = {
        @Index(name = "ix_mov_producto", columnList = "producto_id"),
        @Index(name = "ix_mov_fecha", columnList = "fecha"),
        @Index(name = "ix_mov_origen", columnList = "origen,referenciaId")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MovimientoInventarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha del movimiento */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Producto afectado */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mov_producto"))
    private ProductoEntity producto;

    /** Tipo de movimiento */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipo;

    /** Origen del movimiento (venta/compra/ajuste) */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigenMovimiento origen;

    /** Id de la venta/compra/ajuste (si aplica) */
    private Long referenciaId;

    /** Cantidad (en unidades) */
    @Column(nullable = false)
    private Integer cantidad;

    /** Costo/precio unitario al momento (informativo) */
    @Column(precision = 14, scale = 2)
    private BigDecimal valorUnitario;

    /** Stock del producto después de aplicar el movimiento */
    @Column(nullable = false)
    private Integer stockResultante;

    /** Observaciones opcionales */
    @Column(length = 255)
    private String observaciones;

    @PrePersist
    public void pre() {
        if (fecha == null) fecha = LocalDateTime.now();
    }
}
