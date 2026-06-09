package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.ProductoDTO;
import com.frutimonchis.backend.entity.CategoriaEntity;
import com.frutimonchis.backend.entity.ProductoEntity;
import com.frutimonchis.backend.repository.CategoriaRepository;
import com.frutimonchis.backend.repository.ProductoRepository;
import com.frutimonchis.backend.service.IProductoService;
import com.frutimonchis.backend.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImplement implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    // --------- LISTADOS ----------
    @Override
    public List<ProductoEntity> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public List<ProductoEntity> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoria_Id(categoriaId);
    }

    @Override
    public List<ProductoEntity> buscarPorNombre(String q) {
        return productoRepository.findByNombreContainingIgnoreCase(q);
    }

    @Override
    public List<ProductoEntity> listarPorCategoriaYNombre(Long categoriaId, String q) {
        return productoRepository.findByCategoria_IdAndNombreContainingIgnoreCase(categoriaId, q);
    }

    @Override
    public List<ProductoEntity> listarPorActivo(Boolean activo) {
        return productoRepository.findByActivo(activo);
    }

    // --------- CRUD ----------
    @Override
    public ProductoEntity buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
    }

    @Transactional
    @Override
    public ProductoEntity crear(ProductoDTO dto) {
        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + dto.getCategoriaId()));

        // Validaciones de unicidad
        if (dto.getSku() != null && !dto.getSku().isBlank() && productoRepository.existsBySku(dto.getSku().trim())) {
            throw new IllegalArgumentException("SKU ya existe: " + dto.getSku());
        }
        if (productoRepository.existsByNombreIgnoreCaseAndCategoria_Id(dto.getNombre().trim(), dto.getCategoriaId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre en la categoría indicada.");
        }

        ProductoEntity entity = ProductoEntity.builder()
                .nombre(dto.getNombre().trim())
                .descripcion(dto.getDescripcion())
                .sku(dto.getSku() == null ? null : dto.getSku().trim())
                .unidadMedida(dto.getUnidadMedida())
                .unitMultiplier(dto.getUnitMultiplier())
                .netPrice(dto.getNetPrice())
                .stock(dto.getStock())
                .minStock(dto.getMinStock())
                .imageUrl(dto.getImageUrl())
                .activo(dto.getActivo() == null ? Boolean.TRUE : dto.getActivo())
                .categoria(categoria)
                .build();

        return productoRepository.save(entity);
    }

    @Transactional
    @Override
    public ProductoEntity actualizar(Long id, ProductoDTO dto) {
        ProductoEntity db = buscarPorId(id);

        // Si cambia de categoría, obtener la nueva
        CategoriaEntity categoria = db.getCategoria();
        if (dto.getCategoriaId() != null && !dto.getCategoriaId().equals(db.getCategoria().getId())) {
            categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + dto.getCategoriaId()));
        }

        // Validaciones de unicidad
        if (dto.getSku() != null && !dto.getSku().isBlank()) {
            String nuevoSku = dto.getSku().trim();
            if (!nuevoSku.equals(db.getSku()) && productoRepository.existsBySku(nuevoSku)) {
                throw new IllegalArgumentException("SKU ya existe: " + dto.getSku());
            }
            db.setSku(nuevoSku);
        } else {
            db.setSku(null);
        }

        String nuevoNombre = dto.getNombre().trim();
        boolean cambiaNombreOCategoria = !db.getNombre().equalsIgnoreCase(nuevoNombre)
                || !db.getCategoria().getId().equals(categoria.getId());
        if (cambiaNombreOCategoria &&
                productoRepository.existsByNombreIgnoreCaseAndCategoria_Id(nuevoNombre, categoria.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre en la categoría indicada.");
        }

        db.setNombre(nuevoNombre);
        db.setDescripcion(dto.getDescripcion());
        db.setUnidadMedida(dto.getUnidadMedida());
        db.setUnitMultiplier(dto.getUnitMultiplier());
        db.setNetPrice(dto.getNetPrice());
        db.setStock(dto.getStock());
        db.setMinStock(dto.getMinStock());
        db.setImageUrl(dto.getImageUrl());
        db.setActivo(dto.getActivo() == null ? db.getActivo() : dto.getActivo());
        db.setCategoria(categoria);

        return productoRepository.save(db);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new NotFoundException("Producto no encontrado: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return productoRepository.existsById(id);
    }
}
