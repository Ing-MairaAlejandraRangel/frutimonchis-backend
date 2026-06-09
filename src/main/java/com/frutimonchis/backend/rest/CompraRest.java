package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.CompraCreateDTO;
import com.frutimonchis.backend.entity.CompraEntity;
import com.frutimonchis.backend.entity.EstadoCompra;
import com.frutimonchis.backend.service.ICompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
@CrossOrigin
@RequiredArgsConstructor
public class CompraRest {

    private final ICompraService compraService;

    /**
     * Listados:
     * /compras
     * /compras?proveedorId=1
     * /compras?estado=PENDIENTE
     * /compras?desde=2025-10-01&hasta=2025-10-30
     */
    @GetMapping
    public ResponseEntity<List<CompraEntity>> listar(
            @RequestParam(value = "proveedorId", required = false) Long proveedorId,
            @RequestParam(value = "estado", required = false) EstadoCompra estado,
            @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        if (proveedorId != null) return ResponseEntity.ok(compraService.listarPorProveedor(proveedorId));
        if (estado != null) return ResponseEntity.ok(compraService.listarPorEstado(estado));
        if (desde != null && hasta != null) return ResponseEntity.ok(compraService.listarPorFecha(desde, hasta));
        return ResponseEntity.ok(compraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraEntity> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(compraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CompraEntity> crear(@Valid @RequestBody CompraCreateDTO dto) {
        CompraEntity created = compraService.crear(dto);
        return ResponseEntity.created(URI.create("/api/v1/compras/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<CompraEntity> anular(@PathVariable("id") Long id,
                                               @RequestParam(value = "motivo", required = false) String motivo) {
        return ResponseEntity.ok(compraService.anular(id, motivo));
    }
}
