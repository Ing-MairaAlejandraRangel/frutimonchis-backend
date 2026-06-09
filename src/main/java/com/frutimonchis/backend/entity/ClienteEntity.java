package com.frutimonchis.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "clientes",
        indexes = {
                @Index(name = "ix_cliente_nombre", columnList = "nombre"),
                @Index(name = "ix_cliente_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cliente_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_cliente_documento", columnNames = "documento")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre comercial o razón social */
    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombre;

    /** CC/NIT opcional pero único si se registra */
    @Column(length = 30)
    private String documento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoCliente tipo;

    @Email
    @Column(length = 150, unique = true)
    private String email;

    @Column(length = 25)
    private String telefono;

    @Column(length = 180)
    private String direccion;

    @Column(length = 80)
    private String ciudad;

    /** Saldo a favor del negocio (deuda del cliente). 0 = sin deuda */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private Boolean activo;

    @PrePersist
    public void prePersist() {
        if (saldo == null) saldo = BigDecimal.ZERO;
        if (activo == null) activo = Boolean.TRUE;
        if (tipo == null) tipo = TipoCliente.NATURAL;
    }
}
