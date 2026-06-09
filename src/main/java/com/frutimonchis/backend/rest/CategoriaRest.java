package com.frutimonchis.backend.rest;

import com.frutimonchis.backend.DTO.CategoriaDTO;
import com.frutimonchis.backend.entity.CategoriaEntity;
import com.frutimonchis.backend.service.ICategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@CrossOrigin
@RequiredArgsConstructor
public class CategoriaRest {

    private final ICategoriaService categoriaService;

    @GetMapping
    public List<CategoriaEntity> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    public CategoriaEntity buscar(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaEntity crear(@Valid @RequestBody CategoriaDTO dto) {
        return categoriaService.crear(dto);
    }

    @PutMapping("/{id}")
    public CategoriaEntity actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        return categoriaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }
}
