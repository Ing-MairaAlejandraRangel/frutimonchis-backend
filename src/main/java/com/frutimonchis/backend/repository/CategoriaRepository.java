package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}
