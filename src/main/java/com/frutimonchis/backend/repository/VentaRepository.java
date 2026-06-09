package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.EstadoFactura;
import com.frutimonchis.backend.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<VentaEntity, Long> {

    List<VentaEntity> findByCliente_Id(Long clienteId);

    List<VentaEntity> findByEstado(EstadoFactura estado);

    List<VentaEntity> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}
