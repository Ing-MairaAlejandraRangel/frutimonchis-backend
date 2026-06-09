package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsByNit(String nit);

    List<ProveedorEntity> findByNombreContainingIgnoreCase(String q);
    List<ProveedorEntity> findByActivo(Boolean activo);
    List<ProveedorEntity> findBySaldoPendienteGreaterThan(BigDecimal monto);
}
