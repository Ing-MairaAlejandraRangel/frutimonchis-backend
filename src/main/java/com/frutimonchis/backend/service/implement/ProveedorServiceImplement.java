package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.ProveedorDTO;
import com.frutimonchis.backend.entity.ProveedorEntity;
import com.frutimonchis.backend.repository.ProveedorRepository;
import com.frutimonchis.backend.service.IProveedorService;
import com.frutimonchis.backend.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImplement implements IProveedorService {

    private final ProveedorRepository proveedorRepository;

    // -------- Listados --------
    @Override
    public List<ProveedorEntity> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    public List<ProveedorEntity> buscarPorNombre(String q) {
        return proveedorRepository.findByNombreContainingIgnoreCase(q);
    }

    @Override
    public List<ProveedorEntity> listarPorActivo(Boolean activo) {
        return proveedorRepository.findByActivo(activo);
    }

    @Override
    public List<ProveedorEntity> listarConDeuda() {
        return proveedorRepository.findBySaldoPendienteGreaterThan(BigDecimal.ZERO);
    }

    // -------- CRUD --------
    @Override
    public ProveedorEntity buscarPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado: " + id));
    }

    @Transactional
    @Override
    public ProveedorEntity crear(ProveedorDTO dto) {
        if (dto.getEmail() != null && !dto.getEmail().isBlank()
                && proveedorRepository.existsByEmail(dto.getEmail().trim())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese correo electrónico.");
        }

        if (dto.getNit() != null && !dto.getNit().isBlank()
                && proveedorRepository.existsByNit(dto.getNit().trim())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese NIT.");
        }

        ProveedorEntity p = ProveedorEntity.builder()
                .nombre(dto.getNombre().trim())
                .nit(dto.getNit() == null ? null : dto.getNit().trim())
                .tipo(dto.getTipo())
                .email(dto.getEmail() == null ? null : dto.getEmail().trim())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .saldoPendiente(dto.getSaldoPendiente() == null ? BigDecimal.ZERO : dto.getSaldoPendiente())
                .activo(dto.getActivo() == null ? Boolean.TRUE : dto.getActivo())
                .build();

        return proveedorRepository.save(p);
    }

    @Transactional
    @Override
    public ProveedorEntity actualizar(Long id, ProveedorDTO dto) {
        ProveedorEntity db = buscarPorId(id);

        if (dto.getEmail() != null) {
            String nuevo = dto.getEmail().trim();
            if (nuevo.isBlank()) db.setEmail(null);
            else if (!nuevo.equalsIgnoreCase(db.getEmail()) && proveedorRepository.existsByEmail(nuevo)) {
                throw new IllegalArgumentException("Ya existe un proveedor con ese correo.");
            } else db.setEmail(nuevo);
        }

        if (dto.getNit() != null) {
            String nuevoNit = dto.getNit().trim();
            if (nuevoNit.isBlank()) db.setNit(null);
            else if (!nuevoNit.equalsIgnoreCase(db.getNit()) && proveedorRepository.existsByNit(nuevoNit)) {
                throw new IllegalArgumentException("Ya existe un proveedor con ese NIT.");
            } else db.setNit(nuevoNit);
        }

        if (dto.getNombre() != null) db.setNombre(dto.getNombre().trim());
        if (dto.getTipo() != null) db.setTipo(dto.getTipo());
        if (dto.getTelefono() != null) db.setTelefono(dto.getTelefono());
        if (dto.getDireccion() != null) db.setDireccion(dto.getDireccion());
        if (dto.getCiudad() != null) db.setCiudad(dto.getCiudad());
        if (dto.getSaldoPendiente() != null) {
            if (dto.getSaldoPendiente().compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("El saldo no puede ser negativo.");
            db.setSaldoPendiente(dto.getSaldoPendiente());
        }
        if (dto.getActivo() != null) db.setActivo(dto.getActivo());

        return proveedorRepository.save(db);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id))
            throw new NotFoundException("Proveedor no encontrado: " + id);
        proveedorRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return proveedorRepository.existsById(id);
    }

    // -------- Movimientos --------
    @Transactional
    @Override
    public ProveedorEntity cargarPagoPendiente(Long id, BigDecimal monto, String motivo) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser > 0.");
        ProveedorEntity p = buscarPorId(id);
        p.setSaldoPendiente(p.getSaldoPendiente().add(monto));
        return proveedorRepository.save(p);
    }

    @Transactional
    @Override
    public ProveedorEntity registrarPago(Long id, BigDecimal monto, String referencia) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser > 0.");
        ProveedorEntity p = buscarPorId(id);
        BigDecimal nuevo = p.getSaldoPendiente().subtract(monto);
        if (nuevo.compareTo(BigDecimal.ZERO) < 0) nuevo = BigDecimal.ZERO;
        p.setSaldoPendiente(nuevo);
        return proveedorRepository.save(p);
    }
}
