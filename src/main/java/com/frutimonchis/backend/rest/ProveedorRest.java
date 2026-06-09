package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.ProveedorDTO;
import com.frutimonchis.backend.entity.ProveedorEntity;
import com.frutimonchis.backend.service.IProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proveedores")
@CrossOrigin
@RequiredArgsConstructor
public class ProveedorRest {

    private final IProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorEntity>> listar(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "conDeuda", required = false) Boolean conDeuda
    ) {
        if (Boolean.TRUE.equals(conDeuda)) return ResponseEntity.ok(proveedorService.listarConDeuda());
        if (activo != null) return ResponseEntity.ok(proveedorService.listarPorActivo(activo));
        if (q != null && !q.isBlank()) return ResponseEntity.ok(proveedorService.buscarPorNombre(q));
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorEntity> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(proveedorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProveedorEntity> crear(@Valid @RequestBody ProveedorDTO dto) {
        ProveedorEntity created = proveedorService.crear(dto);
        return ResponseEntity.created(URI.create("/api/v1/proveedores/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorEntity> actualizar(@PathVariable("id") Long id,
                                                      @Valid @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(proveedorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cargar")
    public ResponseEntity<ProveedorEntity> cargar(@PathVariable("id") Long id,
                                                  @RequestParam("monto") BigDecimal monto,
                                                  @RequestParam(value = "motivo", required = false) String motivo) {
        return ResponseEntity.ok(proveedorService.cargarPagoPendiente(id, monto, motivo));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<ProveedorEntity> pagar(@PathVariable("id") Long id,
                                                 @RequestParam("monto") BigDecimal monto,
                                                 @RequestParam(value = "ref", required = false) String ref) {
        return ResponseEntity.ok(proveedorService.registrarPago(id, monto, ref));
    }
}
