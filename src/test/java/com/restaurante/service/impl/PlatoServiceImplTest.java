package com.restaurante.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.Plato;

class PlatoServiceImplTest {

    private PlatoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PlatoServiceImpl();
    }

    private Plato platoDePrueba(String nombre, String categoria) {
        return Plato.builder()
                .nombre(nombre)
                .precio(25000.0)
                .categoria(categoria)
                .descripcion("Descripcion de prueba")
                .disponible(true)
                .build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<Plato> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear asigna un id autogenerado y marca el plato como disponible")
    void crear_debeAsignarIdYQuedarDisponible() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));

        assertNotNull(creado.getId());
        assertTrue(creado.estaDisponible());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("obtenerPorId con un id existente devuelve el plato (caso exitoso)")
    void obtenerPorId_idExistente_debeRetornarElPlato() {
        Plato creado = service.crear(platoDePrueba("Nigiri de salmon", "Nigiri"));

        Plato encontrado = service.obtenerPorId(creado.getId());

        assertEquals(creado.getId(), encontrado.getId());
        assertEquals("Nigiri de salmon", encontrado.getNombre());
    }

    @Test
    @DisplayName("obtenerPorId con un id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_idInexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerDisponibles solo devuelve los platos marcados como disponibles")
    void obtenerDisponibles_debeFiltrarSoloDisponibles() {
        Plato disponible = service.crear(platoDePrueba("Roll California", "Roll"));
        Plato noDisponible = service.crear(platoDePrueba("Roll Especial", "Roll"));
        service.cambiarDisponibilidad(noDisponible.getId(), false);

        List<Plato> disponibles = service.obtenerDisponibles();

        assertEquals(1, disponibles.size());
        assertEquals(disponible.getId(), disponibles.get(0).getId());
    }

    @Test
    @DisplayName("obtenerPorCategoria filtra sin importar mayusculas/minusculas")
    void obtenerPorCategoria_debeFiltrarIgnorandoMayusculas() {
        service.crear(platoDePrueba("Roll California", "Roll"));
        service.crear(platoDePrueba("Sashimi de atun", "Sashimi"));

        List<Plato> rolls = service.obtenerPorCategoria("roll");

        assertEquals(1, rolls.size());
        assertEquals("Roll California", rolls.get(0).getNombre());
    }

    @Test
    @DisplayName("actualizar cambia los datos del plato existente")
    void actualizar_debeActualizarLosDatos() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));
        Plato nuevosDatos = platoDePrueba("Roll California Especial", "Roll");
        nuevosDatos.setPrecio(30000.0);

        Plato actualizado = service.actualizar(creado.getId(), nuevosDatos);

        assertEquals("Roll California Especial", actualizado.getNombre());
        assertEquals(30000.0, actualizado.getPrecio().doubleValue());
    }

    @Test
    @DisplayName("actualizar con un id inexistente lanza RecursoNoEncontradoException")
    void actualizar_idInexistente_debeLanzarExcepcion() {
        Plato nuevosDatos = platoDePrueba("Roll California", "Roll");

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(999L, nuevosDatos));
    }

    @Test
    @DisplayName("cambiarDisponibilidad(false) marca el plato como no disponible")
    void cambiarDisponibilidad_false_debeDesactivarElPlato() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));

        Plato actualizado = service.cambiarDisponibilidad(creado.getId(), false);

        assertFalse(actualizado.estaDisponible());
    }

    @Test
    @DisplayName("eliminar quita el plato de la lista")
    void eliminar_debeQuitarElPlato() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));

        service.eliminar(creado.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con un id inexistente lanza RecursoNoEncontradoException")
    void eliminar_idInexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
