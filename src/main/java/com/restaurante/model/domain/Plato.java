package com.restaurante.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plato del menu de Sushi Craft (rolls, nigiri, sashimi, temaki, etc).
 * Clase de dominio pura: sin anotaciones de Spring, sin DTOs, sin nada
 * que la acople a la infraestructura.
 */
@Data
// genera getters, setters, equals, hashCode, toString
@NoArgsConstructor
// constructor vacio (necesario para deserializacion)
@AllArgsConstructor
// constructor con todos los campos
@Builder
// patron Builder: Plato.builder().nombre("X").build()
public class Plato {

    private Long id;
    private String nombre;
    private Double precio;
    private String categoria; // Roll, Nigiri, Sashimi, Temaki, Entrada, Bebida...
    private Boolean disponible;
    private String descripcion;

    // Comportamiento de negocio propio del objeto: el dominio sabe que
    // puede hacer, sin depender de estado externo.

    public boolean estaDisponible() {
        return Boolean.TRUE.equals(disponible);
    }

    // Activa/desactiva sin que el Service tenga que tocar el campo directamente
    public void activar() {
        this.disponible = true;
    }

    public void desactivar() {
        this.disponible = false;
    }
}
