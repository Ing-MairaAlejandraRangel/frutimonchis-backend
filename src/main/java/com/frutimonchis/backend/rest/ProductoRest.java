package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.ProductoDTO;
import com.frutimonchis.backend.entity.ProductoEntity;
import com.frutimonchis.backend.service.IProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin
@RequiredArgsConstructor
public class ProductoRest {

    private final IProductoService productoService;

    /**
     * Listado con filtros opcionales:
     * - /productos               -> todos
     * - /productos?categoriaId=1 -> por categoría
     * - /productos?q=tom         -> por nombre contiene "tom"
     * - /productos?categoriaId=1&q=tom -> combinación
     * - /productos?activo=true/false -> por estado
     */
    @GetMapping
    public ResponseEntity<List<ProductoEntity>> listar(
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "activo", required = false) Boolean activo
    ) {
        if (activo != null) {
            return ResponseEntity.ok(productoService.listarPorActivo(activo));
        }
        if (categoriaId != null && q != null && !q.isBlank()) {
            return ResponseEntity.ok(productoService.listarPorCategoriaYNombre(categoriaId, q));
        }
        if (categoriaId != null) {
            return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
        }
        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorNombre(q));
        }
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoEntity> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoEntity> crear(@Valid @RequestBody ProductoDTO dto) {
        ProductoEntity created = productoService.crear(dto);
        return ResponseEntity.created(URI.create("/api/v1/productos/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoEntity> actualizar(@PathVariable("id") Long id,
                                                     @Valid @RequestBody ProductoDTO dto) {
        ProductoEntity updated = productoService.actualizar(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
