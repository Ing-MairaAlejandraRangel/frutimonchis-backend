package com.frutimonchis.backend.repository;

import com.frutimonchis.backend.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    boolean existsBySku(String sku);

    boolean existsByNombreIgnoreCaseAndCategoria_Id(String nombre, Long categoriaId);

    List<ProductoEntity> findByCategoria_Id(Long categoriaId);

    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);

    List<ProductoEntity> findByCategoria_IdAndNombreContainingIgnoreCase(Long categoriaId, String nombre);

    List<ProductoEntity> findByActivo(Boolean activo);
}
