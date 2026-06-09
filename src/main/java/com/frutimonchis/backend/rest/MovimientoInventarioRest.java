package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.MovimientoAjusteDTO;
import com.frutimonchis.backend.entity.MovimientoInventarioEntity;
import com.frutimonchis.backend.service.IMovimientoInventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/kardex")
@CrossOrigin
@RequiredArgsConstructor
public class MovimientoInventarioRest {

    private final IMovimientoInventarioService kardexService;

    /**
     * Listados:
     * /kardex?productoId=1&page=0&size=10&sort=fecha,desc
     * /kardex?desde=2025-10-01&hasta=2025-10-30
     * /kardex?productoId=1&desde=2025-10-01&hasta=2025-10-30
     */
    @GetMapping
    public ResponseEntity<Page<MovimientoInventarioEntity>> listar(
            @RequestParam(value = "productoId", required = false) Long productoId,
            @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "fecha", direction = org.springframework.data.domain.Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        if (productoId != null && desde != null && hasta != null) {
            return ResponseEntity.ok(kardexService.listarPorProductoYFechas(productoId, desde, hasta, pageable));
        }
        if (productoId != null) {
            return ResponseEntity.ok(kardexService.listarPorProducto(productoId, pageable));
        }
        if (desde != null && hasta != null) {
            return ResponseEntity.ok(kardexService.listarPorFechas(desde, hasta, pageable));
        }
        // por defecto: todo el histórico paginado
        return ResponseEntity.ok(kardexService.listarPorFechas(LocalDate.now().minusYears(10), LocalDate.now(), pageable));
    }

    /** Ajuste manual (+ o - stock) */
    @PostMapping("/ajuste")
    public ResponseEntity<MovimientoInventarioEntity> ajuste(@Valid @RequestBody MovimientoAjusteDTO dto) {
        MovimientoInventarioEntity creado = kardexService.registrarAjuste(dto);
        return ResponseEntity.created(URI.create("/api/v1/kardex/" + creado.getId())).body(creado);
    }
}
