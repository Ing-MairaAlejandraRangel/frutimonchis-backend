package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.CompraCreateDTO;
import com.frutimonchis.backend.entity.CompraEntity;
import com.frutimonchis.backend.entity.EstadoCompra;

import java.time.LocalDate;
import java.util.List;

public interface ICompraService {

    CompraEntity crear(CompraCreateDTO dto);

    CompraEntity buscarPorId(Long id);

    List<CompraEntity> listar();

    List<CompraEntity> listarPorProveedor(Long proveedorId);

    List<CompraEntity> listarPorEstado(EstadoCompra estado);

    List<CompraEntity> listarPorFecha(LocalDate desde, LocalDate hasta);

    CompraEntity anular(Long id, String motivo);
}
