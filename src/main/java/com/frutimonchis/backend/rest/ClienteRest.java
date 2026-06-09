package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.ClienteDTO;
import com.frutimonchis.backend.entity.ClienteEntity;
import com.frutimonchis.backend.service.IClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin
@RequiredArgsConstructor
public class ClienteRest {

    private final IClienteService clienteService;

    /** Listado con filtros opcionales:
     *  - /clientes                         -> todos
     *  - /clientes?q=maira                 -> por nombre contiene
     *  - /clientes?activo=true/false       -> por estado
     *  - /clientes?deudores=true           -> saldo > 0
     */
    @GetMapping
    public ResponseEntity<List<ClienteEntity>> listar(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "deudores", required = false) Boolean deudores
    ) {
        if (Boolean.TRUE.equals(deudores)) {
            return ResponseEntity.ok(clienteService.listarDeudores());
        }
        if (activo != null) {
            return ResponseEntity.ok(clienteService.listarPorActivo(activo));
        }
        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(clienteService.buscarPorNombre(q));
        }
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteEntity> crear(@Valid @RequestBody ClienteDTO dto) {
        ClienteEntity created = clienteService.crear(dto);
        return ResponseEntity.created(URI.create("/api/v1/clientes/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteEntity> actualizar(@PathVariable("id") Long id,
                                                    @Valid @RequestBody ClienteDTO dto) {
        ClienteEntity updated = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ------- Movimientos de saldo (cargar deuda / abonar pago)
    @PatchMapping("/{id}/cargar")
    public ResponseEntity<ClienteEntity> cargar(@PathVariable("id") Long id,
                                                @RequestParam("monto") BigDecimal monto,
                                                @RequestParam(value = "motivo", required = false) String motivo) {
        return ResponseEntity.ok(clienteService.cargarDeuda(id, monto, motivo));
    }

    @PatchMapping("/{id}/abonar")
    public ResponseEntity<ClienteEntity> abonar(@PathVariable("id") Long id,
                                                @RequestParam("monto") BigDecimal monto,
                                                @RequestParam(value = "ref", required = false) String ref) {
        return ResponseEntity.ok(clienteService.abonarPago(id, monto, ref));
    }
}
