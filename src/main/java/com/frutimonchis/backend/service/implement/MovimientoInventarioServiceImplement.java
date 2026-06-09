package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.MovimientoAjusteDTO;
import com.frutimonchis.backend.entity.*;
import com.frutimonchis.backend.repository.MovimientoInventarioRepository;
import com.frutimonchis.backend.repository.ProductoRepository;
import com.frutimonchis.backend.service.IMovimientoInventarioService;
import com.frutimonchis.backend.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImplement implements IMovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepo;
    private final ProductoRepository productoRepo;

    @Transactional
    @Override
    public MovimientoInventarioEntity registrarEntrada(Long productoId, int cantidad, BigDecimal valorUnitario,
                                                       Long referenciaId, String obs) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad de entrada debe ser > 0");
        ProductoEntity p = productoRepo.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + productoId));
        // sumar stock
        p.setStock(p.getStock() + cantidad);
        productoRepo.save(p);

        MovimientoInventarioEntity mov = MovimientoInventarioEntity.builder()
                .fecha(LocalDateTime.now())
                .producto(p)
                .tipo(TipoMovimiento.ENTRADA)
                .origen(OrigenMovimiento.COMPRA)
                .referenciaId(referenciaId)
                .cantidad(cantidad)
                .valorUnitario(valorUnitario)
                .stockResultante(p.getStock())
                .observaciones(obs)
                .build();

        return movimientoRepo.save(mov);
    }

    @Transactional
    @Override
    public MovimientoInventarioEntity registrarSalida(Long productoId, int cantidad, BigDecimal valorUnitario,
                                                      Long referenciaId, String obs) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad de salida debe ser > 0");
        ProductoEntity p = productoRepo.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + productoId));
        // restar stock (validación de stock debe hacerse antes en capa de negocio)
        int nuevo = p.getStock() - cantidad;
        if (nuevo < 0) nuevo = 0;
        p.setStock(nuevo);
        productoRepo.save(p);

        MovimientoInventarioEntity mov = MovimientoInventarioEntity.builder()
                .fecha(LocalDateTime.now())
                .producto(p)
                .tipo(TipoMovimiento.SALIDA)
                .origen(OrigenMovimiento.VENTA)
                .referenciaId(referenciaId)
                .cantidad(cantidad)
                .valorUnitario(valorUnitario)
                .stockResultante(p.getStock())
                .observaciones(obs)
                .build();

        return movimientoRepo.save(mov);
    }

    @Transactional
    @Override
    public MovimientoInventarioEntity registrarAjuste(MovimientoAjusteDTO dto) {
        ProductoEntity p = productoRepo.findById(dto.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + dto.getProductoId()));

        int cant = dto.getCantidad(); // puede ser negativa
        int nuevo = p.getStock() + cant;
        if (nuevo < 0) nuevo = 0; // sin negativos
        p.setStock(nuevo);
        productoRepo.save(p);

        TipoMovimiento tipo = TipoMovimiento.AJUSTE;

        MovimientoInventarioEntity mov = MovimientoInventarioEntity.builder()
                .fecha(LocalDateTime.now())
                .producto(p)
                .tipo(tipo)
                .origen(OrigenMovimiento.AJUSTE)
                .referenciaId(null)
                .cantidad(cant)
                .valorUnitario(dto.getValorUnitario())
                .stockResultante(p.getStock())
                .observaciones(dto.getObservaciones())
                .build();

        return movimientoRepo.save(mov);
    }

    @Override
    public Page<MovimientoInventarioEntity> listarPorProducto(Long productoId, Pageable pageable) {
        return movimientoRepo.findByProducto_Id(productoId, pageable);
    }

    @Override
    public Page<MovimientoInventarioEntity> listarPorFechas(LocalDate desde, LocalDate hasta, Pageable pageable) {
        LocalDateTime d = desde.atStartOfDay();
        LocalDateTime h = hasta.plusDays(1).atStartOfDay().minusSeconds(1);
        return movimientoRepo.findByFechaBetween(d, h, pageable);
    }

    @Override
    public Page<MovimientoInventarioEntity> listarPorProductoYFechas(Long productoId, LocalDate desde, LocalDate hasta, Pageable pageable) {
        LocalDateTime d = desde.atStartOfDay();
        LocalDateTime h = hasta.plusDays(1).atStartOfDay().minusSeconds(1);
        return movimientoRepo.findByProducto_IdAndFechaBetween(productoId, d, h, pageable);
    }
}
