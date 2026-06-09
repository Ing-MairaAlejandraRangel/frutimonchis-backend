package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.CompraCreateDTO;
import com.frutimonchis.backend.DTO.CompraItemDTO;
import com.frutimonchis.backend.entity.*;
import com.frutimonchis.backend.repository.CompraRepository;
import com.frutimonchis.backend.repository.ProductoRepository;
import com.frutimonchis.backend.repository.ProveedorRepository;
import com.frutimonchis.backend.service.ICompraService;
import com.frutimonchis.backend.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImplement implements ICompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    @Override
    public CompraEntity crear(CompraCreateDTO dto) {
        // 1) Proveedor
        ProveedorEntity proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado: " + dto.getProveedorId()));

        // 2) Crear compra en memoria
        CompraEntity compra = CompraEntity.builder()
                .fecha(LocalDateTime.now())
                .proveedor(proveedor)
                .metodoPago(dto.getMetodoPago())
                .observaciones(dto.getObservaciones())
                .total(BigDecimal.ZERO)
                .totalPagado(dto.getTotalPagado())
                .saldo(BigDecimal.ZERO)
                .estado(EstadoCompra.PENDIENTE)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // 3) Procesar ítems: aumentar stock, construir detalle
        for (CompraItemDTO item : dto.getItems()) {
            ProductoEntity prod = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + item.getProductoId()));

            BigDecimal costoUnitario = item.getCostoUnitario();
            if (costoUnitario.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("El costo unitario debe ser > 0");

            // aumentar stock
            prod.setStock(prod.getStock() + item.getCantidad());
            productoRepository.save(prod);

            BigDecimal subtotal = costoUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

            CompraDetalleEntity det = CompraDetalleEntity.builder()
                    .compra(compra)
                    .producto(prod)
                    .nombreProducto(prod.getNombre())
                    .unidadMedida(prod.getUnidadMedida())
                    .cantidad(item.getCantidad())
                    .costoUnitario(costoUnitario)
                    .subtotal(subtotal)
                    .build();

            compra.addDetalle(det);
            total = total.add(subtotal);
        }

        // 4) Totales y estado
        compra.setTotal(total);
        BigDecimal pagado = dto.getTotalPagado() == null ? BigDecimal.ZERO : dto.getTotalPagado();
        compra.setTotalPagado(pagado);
        BigDecimal saldo = total.subtract(pagado);
        if (saldo.compareTo(BigDecimal.ZERO) < 0) saldo = BigDecimal.ZERO; // no permitir negativo
        compra.setSaldo(saldo);
        compra.setEstado(saldo.compareTo(BigDecimal.ZERO) == 0 ? EstadoCompra.PAGADA : EstadoCompra.PENDIENTE);

        // 5) Actualizar saldo del proveedor si quedó pendiente
        if (saldo.compareTo(BigDecimal.ZERO) > 0) {
            proveedor.setSaldoPendiente(proveedor.getSaldoPendiente().add(saldo));
            proveedorRepository.save(proveedor);
        }

        // 6) Guardar compra completa
        return compraRepository.save(compra);
    }

    @Override
    public CompraEntity buscarPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compra no encontrada: " + id));
    }

    @Override
    public List<CompraEntity> listar() {
        return compraRepository.findAll();
    }

    @Override
    public List<CompraEntity> listarPorProveedor(Long proveedorId) {
        return compraRepository.findByProveedor_Id(proveedorId);
    }

    @Override
    public List<CompraEntity> listarPorEstado(EstadoCompra estado) {
        return compraRepository.findByEstado(estado);
    }

    @Override
    public List<CompraEntity> listarPorFecha(LocalDate desde, LocalDate hasta) {
        LocalDateTime d = desde.atStartOfDay();
        LocalDateTime h = hasta.plusDays(1).atStartOfDay().minusSeconds(1);
        return compraRepository.findByFechaBetween(d, h);
    }

    @Transactional
    @Override
    public CompraEntity anular(Long id, String motivo) {
        CompraEntity compra = buscarPorId(id);
        if (compra.getEstado() == EstadoCompra.ANULADA) return compra;

        // Revertir stock
        for (CompraDetalleEntity det : compra.getDetalles()) {
            ProductoEntity p = det.getProducto();
            int nuevoStock = p.getStock() - det.getCantidad();
            if (nuevoStock < 0) nuevoStock = 0; // nunca dejar negativo
            p.setStock(nuevoStock);
            productoRepository.save(p);
        }

        // Revertir saldo del proveedor si había saldo
        if (compra.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            ProveedorEntity prov = compra.getProveedor();
            BigDecimal nuevo = prov.getSaldoPendiente().subtract(compra.getSaldo());
            if (nuevo.compareTo(BigDecimal.ZERO) < 0) nuevo = BigDecimal.ZERO;
            prov.setSaldoPendiente(nuevo);
            proveedorRepository.save(prov);
        }

        compra.setEstado(EstadoCompra.ANULADA);
        String obs = (compra.getObservaciones() == null ? "" : compra.getObservaciones() + " | ");
        compra.setObservaciones(obs + "ANULADA: " + (motivo == null ? "" : motivo));
        return compraRepository.save(compra);
    }
}
