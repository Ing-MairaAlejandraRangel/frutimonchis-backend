package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.MovimientoAjusteDTO;
import com.frutimonchis.backend.entity.MovimientoInventarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IMovimientoInventarioService {

    // Registro automático
    MovimientoInventarioEntity registrarEntrada(Long productoId, int cantidad, java.math.BigDecimal valorUnitario,
                                                Long referenciaId, String obs);

    MovimientoInventarioEntity registrarSalida(Long productoId, int cantidad, java.math.BigDecimal valorUnitario,
                                               Long referenciaId, String obs);

    // Ajuste manual (+/-)
    MovimientoInventarioEntity registrarAjuste(MovimientoAjusteDTO dto);

    // Consultas
    Page<MovimientoInventarioEntity> listarPorProducto(Long productoId, Pageable pageable);

    Page<MovimientoInventarioEntity> listarPorFechas(LocalDate desde, LocalDate hasta, Pageable pageable);

    Page<MovimientoInventarioEntity> listarPorProductoYFechas(Long productoId, LocalDate desde, LocalDate hasta, Pageable pageable);
}
