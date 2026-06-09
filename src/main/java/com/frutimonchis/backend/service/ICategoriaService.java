package com.frutimonchis.backend.service;

import com.frutimonchis.backend.DTO.CategoriaDTO;
import com.frutimonchis.backend.entity.CategoriaEntity;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaEntity> listar();
    CategoriaEntity buscarPorId(Long id);
    CategoriaEntity crear(CategoriaDTO dto);
    CategoriaEntity actualizar(Long id, CategoriaDTO dto);
    void eliminar(Long id);
}
