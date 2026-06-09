package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsByDocumento(String documento);

    List<ClienteEntity> findByNombreContainingIgnoreCase(String q);
    List<ClienteEntity> findByActivo(Boolean activo);

    /** deudores = saldo > 0 */
    List<ClienteEntity> findBySaldoGreaterThan(BigDecimal cero);

    /** combinaciones útiles */
    List<ClienteEntity> findByNombreContainingIgnoreCaseAndActivo(String q, Boolean activo);
}
