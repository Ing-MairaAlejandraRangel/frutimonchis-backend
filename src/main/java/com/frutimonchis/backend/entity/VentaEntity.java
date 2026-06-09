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
@Table(name = "ventas", indexes = {
        @Index(name = "ix_venta_fecha", columnList = "fecha"),
        @Index(name = "ix_venta_estado", columnList = "estado")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class VentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora de la venta */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Cliente que compra */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_venta_cliente"))
    private ClienteEntity cliente;

    /** Método principal con el que paga (para registro rápido) */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MetodoPago metodoPago;

    /** Estado de la factura */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFactura estado;

    /** Observaciones libres en la venta */
    @Column(length = 255)
    private String observaciones;

    /** Totales */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalPagado;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal saldo; // total - totalPagado

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private List<VentaDetalleEntity> detalles = new ArrayList<>();

    public void addDetalle(VentaDetalleEntity d) {
        d.setVenta(this);
        this.detalles.add(d);
    }

    @PrePersist
    public void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
        if (total == null) total = BigDecimal.ZERO;
        if (totalPagado == null) totalPagado = BigDecimal.ZERO;
        if (saldo == null) saldo = total.subtract(totalPagado);
        if (estado == null) estado = (saldo.compareTo(BigDecimal.ZERO) > 0) ? EstadoFactura.PENDIENTE : EstadoFactura.PAGADA;
    }
}
