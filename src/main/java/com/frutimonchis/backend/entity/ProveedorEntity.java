package com.frutimonchis.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "proveedores",
        indexes = {
                @Index(name = "ix_proveedor_nombre", columnList = "nombre"),
                @Index(name = "ix_proveedor_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_proveedor_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_proveedor_nit", columnNames = "nit")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProveedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre o razón social del proveedor */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombre;

    /** NIT o CC, debe ser único */
    @Column(length = 30, unique = true)
    private String nit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoProveedor tipo;

    @Email
    @Column(length = 150, unique = true)
    private String email;

    @Column(length = 25)
    private String telefono;

    @Column(length = 180)
    private String direccion;

    @Column(length = 80)
    private String ciudad;

    /** Saldo que el negocio le debe al proveedor */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal saldoPendiente;

    @Column(nullable = false)
    private Boolean activo;

    @PrePersist
    public void prePersist() {
        if (saldoPendiente == null) saldoPendiente = BigDecimal.ZERO;
        if (activo == null) activo = Boolean.TRUE;
        if (tipo == null) tipo = TipoProveedor.EMPRESA;
    }
}
