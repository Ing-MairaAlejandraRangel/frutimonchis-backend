package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.ProductoDTO;
import com.frutimonchis.backend.entity.ProductoEntity;

import java.util.List;

public interface IProductoService {

    // Listados
    List<ProductoEntity> listarTodos();
    List<ProductoEntity> listarPorCategoria(Long categoriaId);
    List<ProductoEntity> buscarPorNombre(String q);
    List<ProductoEntity> listarPorCategoriaYNombre(Long categoriaId, String q);
    List<ProductoEntity> listarPorActivo(Boolean activo);

    // CRUD
    ProductoEntity buscarPorId(Long id);
    ProductoEntity crear(ProductoDTO dto);
    ProductoEntity actualizar(Long id, ProductoDTO dto);
    void eliminar(Long id);

    boolean existePorId(Long id);
}
