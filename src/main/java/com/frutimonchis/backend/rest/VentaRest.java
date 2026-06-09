package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.VentaCreateDTO;
import com.frutimonchis.backend.entity.EstadoFactura;
import com.frutimonchis.backend.entity.VentaEntity;
import com.frutimonchis.backend.service.IVentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@CrossOrigin
@RequiredArgsConstructor
public class VentaRest {

    private final IVentaService ventaService;

    /**
     * Listados:
     * /ventas
     * /ventas?clienteId=1
     * /ventas?estado=PENDIENTE
     * /ventas?desde=2025-10-01&hasta=2025-10-29
     */
    @GetMapping
    public ResponseEntity<List<VentaEntity>> listar(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @RequestParam(value = "estado", required = false) EstadoFactura estado,
            @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        if (clienteId != null) return ResponseEntity.ok(ventaService.listarPorCliente(clienteId));
        if (estado != null)    return ResponseEntity.ok(ventaService.listarPorEstado(estado));
        if (desde != null && hasta != null) return ResponseEntity.ok(ventaService.listarPorFecha(desde, hasta));
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaEntity> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VentaEntity> crear(@Valid @RequestBody VentaCreateDTO dto) {
        VentaEntity created = ventaService.crear(dto);
        return ResponseEntity.created(URI.create("/api/v1/ventas/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<VentaEntity> anular(@PathVariable("id") Long id,
                                              @RequestParam(value = "motivo", required = false) String motivo) {
        return ResponseEntity.ok(ventaService.anular(id, motivo));
    }
}
