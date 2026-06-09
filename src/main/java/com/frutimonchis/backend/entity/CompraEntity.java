package com.frutimonchis.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras", indexes = {
        @Index(name = "ix_compra_fecha", columnList = "fecha"),
        @Index(name = "ix_compra_estado", columnList = "estado")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CompraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora de la compra */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Proveedor al que se compra */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_compra_proveedor"))
    private ProveedorEntity proveedor;

    /** Método de pago principal usado en la operación */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MetodoPago metodoPago;

    /** Estado */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCompra estado;

    /** Observaciones */
    @Column(length = 255)
    private String observaciones;

    /** Totales */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalPagado;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal saldo; // total - totalPagado

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private List<CompraDetalleEntity> detalles = new ArrayList<>();

    public void addDetalle(CompraDetalleEntity d) {
        d.setCompra(this);
        this.detalles.add(d);
    }

    @PrePersist
    public void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
        if (total == null) total = BigDecimal.ZERO;
        if (totalPagado == null) totalPagado = BigDecimal.ZERO;
        if (saldo == null) saldo = total.subtract(totalPagado);
        if (estado == null) estado = (saldo.compareTo(BigDecimal.ZERO) > 0) ? EstadoCompra.PENDIENTE : EstadoCompra.PAGADA;
    }
}
