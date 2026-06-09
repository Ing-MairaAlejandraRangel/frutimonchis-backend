package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.ProveedorDTO;
import com.frutimonchis.backend.entity.ProveedorEntity;

import java.math.BigDecimal;
import java.util.List;

public interface IProveedorService {

    // Listados y filtros
    List<ProveedorEntity> listarTodos();
    List<ProveedorEntity> buscarPorNombre(String q);
    List<ProveedorEntity> listarPorActivo(Boolean activo);
    List<ProveedorEntity> listarConDeuda();

    // CRUD
    ProveedorEntity buscarPorId(Long id);
    ProveedorEntity crear(ProveedorDTO dto);
    ProveedorEntity actualizar(Long id, ProveedorDTO dto);
    void eliminar(Long id);
    boolean existePorId(Long id);

    // Movimientos de cuenta
    ProveedorEntity cargarPagoPendiente(Long id, BigDecimal monto, String motivo);
    ProveedorEntity registrarPago(Long id, BigDecimal monto, String referencia);
}
