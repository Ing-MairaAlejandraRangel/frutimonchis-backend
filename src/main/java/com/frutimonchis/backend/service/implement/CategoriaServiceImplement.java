package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.CategoriaDTO;
import com.frutimonchis.backend.entity.CategoriaEntity;
import com.frutimonchis.backend.repository.CategoriaRepository;
import com.frutimonchis.backend.service.ICategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImplement implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaEntity> listar() {
        return categoriaRepository.findAll();
    }

    @Override
    public CategoriaEntity buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + id));
    }

    @Transactional
    @Override
    public CategoriaEntity crear(CategoriaDTO dto) {
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        CategoriaEntity entity = CategoriaEntity.builder()
                .nombre(dto.getNombre().trim())
                .descripcion(dto.getDescripcion())
                .build();
        return categoriaRepository.save(entity);
    }

    @Transactional
    @Override
    public CategoriaEntity actualizar(Long id, CategoriaDTO dto) {
        CategoriaEntity db = buscarPorId(id);
        // Si cambia el nombre, validar unicidad
        if (!db.getNombre().equalsIgnoreCase(dto.getNombre())
                && categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        db.setNombre(dto.getNombre().trim());
        db.setDescripcion(dto.getDescripcion());
        return categoriaRepository.save(db);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
