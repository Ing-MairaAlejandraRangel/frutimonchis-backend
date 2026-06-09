package com.frutimonchis.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias",
        uniqueConstraints = @UniqueConstraint(name = "uk_categoria_nombre", columnNames = "nombre"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
