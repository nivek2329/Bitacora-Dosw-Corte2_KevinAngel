package com.restaurante.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de salida para un Plato. Solo los campos que el cliente
 * necesita ver - nunca se expone el objeto de dominio directamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatoResponseDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private String categoria;
    private Boolean disponible;
    private String descripcion;
}
