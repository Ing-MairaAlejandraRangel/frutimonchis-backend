package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.CompraEntity;
import com.frutimonchis.backend.entity.EstadoCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CompraRepository extends JpaRepository<CompraEntity, Long> {

    List<CompraEntity> findByProveedor_Id(Long proveedorId);

    List<CompraEntity> findByEstado(EstadoCompra estado);

    List<CompraEntity> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}
