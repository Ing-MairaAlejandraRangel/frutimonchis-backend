package com.frutimonchis.backend.service.implement;

import com.frutimonchis.backend.DTO.ClienteDTO;
import com.frutimonchis.backend.entity.ClienteEntity;
import com.frutimonchis.backend.repository.ClienteRepository;
import com.frutimonchis.backend.service.IClienteService;
import com.frutimonchis.backend.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImplement implements IClienteService {

    private final ClienteRepository clienteRepository;

    // ------- Listados
    @Override
    public List<ClienteEntity> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public List<ClienteEntity> buscarPorNombre(String q) {
        return clienteRepository.findByNombreContainingIgnoreCase(q);
    }

    @Override
    public List<ClienteEntity> listarPorActivo(Boolean activo) {
        return clienteRepository.findByActivo(activo);
    }

    @Override
    public List<ClienteEntity> listarDeudores() {
        return clienteRepository.findBySaldoGreaterThan(BigDecimal.ZERO);
    }

    // ------- CRUD
    @Override
    public ClienteEntity buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + id));
    }

    @Transactional
    @Override
    public ClienteEntity crear(ClienteDTO dto) {
        // Unicidades opcionales si vienen
        if (dto.getEmail() != null && !dto.getEmail().isBlank()
                && clienteRepository.existsByEmail(dto.getEmail().trim())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese correo.");
        }
        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()
                && clienteRepository.existsByDocumento(dto.getDocumento().trim())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese documento.");
        }

        ClienteEntity c = ClienteEntity.builder()
                .nombre(dto.getNombre().trim())
                .documento(dto.getDocumento() == null ? null : dto.getDocumento().trim())
                .tipo(dto.getTipo())
                .email(dto.getEmail() == null ? null : dto.getEmail().trim())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .saldo(dto.getSaldo() == null ? BigDecimal.ZERO : dto.getSaldo())
                .activo(dto.getActivo() == null ? Boolean.TRUE : dto.getActivo())
                .build();

        return clienteRepository.save(c);
    }

    @Transactional
    @Override
    public ClienteEntity actualizar(Long id, ClienteDTO dto) {
        ClienteEntity db = buscarPorId(id);

        // Validar email si cambia
        if (dto.getEmail() != null) {
            String nuevo = dto.getEmail().trim();
            if (nuevo.isBlank()) {
                db.setEmail(null);
            } else if (!nuevo.equalsIgnoreCase(db.getEmail())
                    && clienteRepository.existsByEmail(nuevo)) {
                throw new IllegalArgumentException("Ya existe un cliente con ese correo.");
            } else {
                db.setEmail(nuevo);
            }
        }

        // Validar documento si cambia
        if (dto.getDocumento() != null) {
            String nuevoDoc = dto.getDocumento().trim();
            if (nuevoDoc.isBlank()) {
                db.setDocumento(null);
            } else if (!nuevoDoc.equalsIgnoreCase(db.getDocumento())
                    && clienteRepository.existsByDocumento(nuevoDoc)) {
                throw new IllegalArgumentException("Ya existe un cliente con ese documento.");
            } else {
                db.setDocumento(nuevoDoc);
            }
        }

        if (dto.getNombre() != null) db.setNombre(dto.getNombre().trim());
        if (dto.getTipo() != null) db.setTipo(dto.getTipo());
        if (dto.getTelefono() != null) db.setTelefono(dto.getTelefono());
        if (dto.getDireccion() != null) db.setDireccion(dto.getDireccion());
        if (dto.getCiudad() != null) db.setCiudad(dto.getCiudad());
        if (dto.getSaldo() != null) {
            if (dto.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El saldo no puede ser negativo.");
            }
            db.setSaldo(dto.getSaldo());
        }
        if (dto.getActivo() != null) db.setActivo(dto.getActivo());

        return clienteRepository.save(db);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new NotFoundException("Cliente no encontrado: " + id);
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return clienteRepository.existsById(id);
    }

    // ------- Movimientos de saldo
    @Transactional
    @Override
    public ClienteEntity cargarDeuda(Long id, BigDecimal monto, String motivo) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser > 0.");
        ClienteEntity c = buscarPorId(id);
        c.setSaldo(c.getSaldo().add(monto));
        // (motivo lo dejamos por ahora para auditoría futura)
        return clienteRepository.save(c);
    }

    @Transactional
    @Override
    public ClienteEntity abonarPago(Long id, BigDecimal monto, String referencia) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser > 0.");
        ClienteEntity c = buscarPorId(id);
        BigDecimal nuevo = c.getSaldo().subtract(monto);
        if (nuevo.compareTo(BigDecimal.ZERO) < 0) nuevo = BigDecimal.ZERO; // no dejar negativo
        c.setSaldo(nuevo);
        // (referencia queda para futuras tablas de movimientos)
        return clienteRepository.save(c);
    }
}
