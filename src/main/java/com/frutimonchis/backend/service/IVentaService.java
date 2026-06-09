package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.VentaCreateDTO;
import com.frutimonchis.backend.entity.EstadoFactura;
import com.frutimonchis.backend.entity.VentaEntity;

import java.time.LocalDate;
import java.util.List;

public interface IVentaService {

    VentaEntity crear(VentaCreateDTO dto);

    VentaEntity buscarPorId(Long id);

    List<VentaEntity> listar();

    List<VentaEntity> listarPorCliente(Long clienteId);

    List<VentaEntity> listarPorEstado(EstadoFactura estado);

    List<VentaEntity> listarPorFecha(LocalDate desde, LocalDate hasta);

    VentaEntity anular(Long id, String motivo);
}
