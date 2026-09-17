package com.restaurante.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.restaurante.model.domain.Plato;
import com.restaurante.model.dto.request.PlatoRequestDTO;
import com.restaurante.model.dto.response.PlatoResponseDTO;

/**
 * Traductor entre DTOs y dominio para Plato. Vive en el Controller, no
 * en el Service. MapStruct genera la implementacion en tiempo de
 * compilacion (target/generated-sources/annotations/PlatoMapperImpl.java).
 */
@Mapper(componentModel = "spring")
public interface PlatoMapper {

    // ---- MapperIn: RequestDTO -> Dominio -------------------------------
    // nombre, precio, categoria y descripcion tienen el mismo nombre y
    // tipo en ambos lados -> MapStruct los mapea solo.
    @Mapping(target = "id", ignore = true) // el id lo asigna el Service
    @Mapping(target = "disponible", constant = "true") // todo plato nuevo empieza disponible
    Plato toDomain(PlatoRequestDTO dto);

    // ---- MapperOut: Dominio -> ResponseDTO -----------------------------
    // Todos los campos coinciden - MapStruct los mapea sin anotaciones.
    PlatoResponseDTO toResponse(Plato plato);

    // ---- Lista: MapStruct genera el bucle automaticamente --------------
    List<PlatoResponseDTO> toResponseList(List<Plato> platos);
}
