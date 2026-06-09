package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.VentaCreateDTO;
import com.frutimonchis.backend.DTO.VentaItemDTO;
import com.frutimonchis.backend.entity.*;
import com.frutimonchis.backend.repository.ClienteRepository;
import com.frutimonchis.backend.repository.ProductoRepository;
import com.frutimonchis.backend.repository.VentaRepository;
import com.frutimonchis.backend.service.IVentaService;
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
public class VentaServiceImplement implements IVentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    @Override
    public VentaEntity crear(VentaCreateDTO dto) {
        // 1) Cliente
        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + dto.getClienteId()));

        // 2) Construir venta y calcular totales
        VentaEntity venta = VentaEntity.builder()
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .metodoPago(dto.getMetodoPago())
                .observaciones(dto.getObservaciones())
                .total(BigDecimal.ZERO)
                .totalPagado(dto.getTotalPagado())
                .saldo(BigDecimal.ZERO)
                .estado(EstadoFactura.PENDIENTE) // se recalcula abajo
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // 3) Procesar ítems: validar stock, descontar, crear detalle
        for (VentaItemDTO item : dto.getItems()) {
            ProductoEntity prod = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + item.getProductoId()));

            // precio unitario: el enviado o el netPrice del producto
            BigDecimal pUnit = (item.getPrecioUnitario() != null) ? item.getPrecioUnitario() : prod.getNetPrice();
            if (pUnit == null) throw new IllegalArgumentException("El producto no tiene precio configurado.");

            // validar stock suficiente
            if (prod.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + prod.getNombre());
            }

            // descontar stock
            prod.setStock(prod.getStock() - item.getCantidad());
            productoRepository.save(prod);

            BigDecimal subtotal = pUnit.multiply(BigDecimal.valueOf(item.getCantidad()));

            VentaDetalleEntity detalle = VentaDetalleEntity.builder()
                    .venta(venta)
                    .producto(prod)
                    .nombreProducto(prod.getNombre())
                    .unidadMedida(prod.getUnidadMedida())
                    .cantidad(item.getCantidad())
                    .precioUnitario(pUnit)
                    .subtotal(subtotal)
                    .build();

            venta.addDetalle(detalle);
            total = total.add(subtotal);
        }

        // 4) Totales y estado
        venta.setTotal(total);
        BigDecimal pagado = dto.getTotalPagado() == null ? BigDecimal.ZERO : dto.getTotalPagado();
        venta.setTotalPagado(pagado);
        BigDecimal saldo = total.subtract(pagado);
        if (saldo.compareTo(BigDecimal.ZERO) < 0) saldo = BigDecimal.ZERO; // no permitir negativo
        venta.setSaldo(saldo);
        venta.setEstado(saldo.compareTo(BigDecimal.ZERO) == 0 ? EstadoFactura.PAGADA : EstadoFactura.PENDIENTE);

        // 5) Actualizar saldo del cliente si quedó deuda
        if (saldo.compareTo(BigDecimal.ZERO) > 0) {
            cliente.setSaldo(cliente.getSaldo().add(saldo));
            clienteRepository.save(cliente);
        }

        // 6) Guardar venta completa
        return ventaRepository.save(venta);
    }

    @Override
    public VentaEntity buscarPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada: " + id));
    }

    @Override
    public List<VentaEntity> listar() {
        return ventaRepository.findAll();
    }

    @Override
    public List<VentaEntity> listarPorCliente(Long clienteId) {
        return ventaRepository.findByCliente_Id(clienteId);
    }

    @Override
    public List<VentaEntity> listarPorEstado(EstadoFactura estado) {
        return ventaRepository.findByEstado(estado);
    }

    @Override
    public List<VentaEntity> listarPorFecha(LocalDate desde, LocalDate hasta) {
        LocalDateTime d = desde.atStartOfDay();
        LocalDateTime h = hasta.plusDays(1).atStartOfDay().minusSeconds(1);
        return ventaRepository.findByFechaBetween(d, h);
    }

    @Transactional
    @Override
    public VentaEntity anular(Long id, String motivo) {
        VentaEntity venta = buscarPorId(id);
        if (venta.getEstado() == EstadoFactura.ANULADA) return venta;

        // Revertir stock
        for (VentaDetalleEntity det : venta.getDetalles()) {
            ProductoEntity p = det.getProducto();
            p.setStock(p.getStock() + det.getCantidad());
            productoRepository.save(p);
        }

        // Revertir saldo del cliente si había deuda
        if (venta.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            ClienteEntity c = venta.getCliente();
            BigDecimal nuevo = c.getSaldo().subtract(venta.getSaldo());
            if (nuevo.compareTo(BigDecimal.ZERO) < 0) nuevo = BigDecimal.ZERO;
            c.setSaldo(nuevo);
            clienteRepository.save(c);
        }

        venta.setEstado(EstadoFactura.ANULADA);
        String obs = (venta.getObservaciones() == null ? "" : venta.getObservaciones() + " | ");
        venta.setObservaciones(obs + "ANULADA: " + (motivo == null ? "" : motivo));
        return ventaRepository.save(venta);
    }
}
