package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.ClienteDTO;
import com.frutimonchis.backend.entity.ClienteEntity;

import java.math.BigDecimal;
import java.util.List;

public interface IClienteService {

    // listados / filtros
    List<ClienteEntity> listarTodos();
    List<ClienteEntity> buscarPorNombre(String q);
    List<ClienteEntity> listarPorActivo(Boolean activo);
    List<ClienteEntity> listarDeudores();

    // CRUD
    ClienteEntity buscarPorId(Long id);
    ClienteEntity crear(ClienteDTO dto);
    ClienteEntity actualizar(Long id, ClienteDTO dto);
    void eliminar(Long id);
    boolean existePorId(Long id);

    // Movimientos de saldo
    ClienteEntity cargarDeuda(Long id, BigDecimal monto, String motivo);
    ClienteEntity abonarPago(Long id, BigDecimal monto, String referencia);
}
