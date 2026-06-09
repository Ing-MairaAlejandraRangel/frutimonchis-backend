package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.MovimientoInventarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventarioEntity, Long> {

    Page<MovimientoInventarioEntity> findByProducto_Id(Long productoId, Pageable pageable);

    Page<MovimientoInventarioEntity> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<MovimientoInventarioEntity> findByProducto_IdAndFechaBetween(Long productoId, LocalDateTime desde, LocalDateTime hasta, Pageable pageable);
}
