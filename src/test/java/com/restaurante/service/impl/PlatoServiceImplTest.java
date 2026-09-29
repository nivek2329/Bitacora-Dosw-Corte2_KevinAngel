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
    @DisplayName("obtenerTodos devuelve todos los platos creados")
    void obtenerTodos_debeRetornarTodosLosPlatos() {
        service.crear(platoDePrueba("Roll California", "Roll"));
        service.crear(platoDePrueba("Sashimi de atun", "Sashimi"));

        List<Plato> resultado = service.obtenerTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("obtenerPorId con id existente devuelve el plato")
    void obtenerPorId_existente_debeRetornarPlato() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));

        Plato resultado = service.obtenerPorId(creado.getId());

        assertEquals("Roll California", resultado.getNombre());
    }

    @Test
    @DisplayName("obtenerPorId con id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("crear asigna un id y guarda el plato")
    void crear_debeAsignarIdYGuardar() {
        Plato resultado = service.crear(platoDePrueba("Nigiri de salmon", "Nigiri"));

        assertNotNull(resultado.getId());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("actualizar con id existente modifica los campos del plato")
    void actualizar_existente_debeActualizarCampos() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));
        Plato nuevosDatos = platoDePrueba("Roll California Especial", "Roll");
        nuevosDatos.setPrecio(28000.0);

        Plato resultado = service.actualizar(creado.getId(), nuevosDatos);

        assertEquals("Roll California Especial", resultado.getNombre());
        assertEquals(28000.0, resultado.getPrecio());
    }

    @Test
    @DisplayName("actualizar con id inexistente lanza RecursoNoEncontradoException")
    void actualizar_inexistente_debeLanzarExcepcion() {
        Plato nuevosDatos = platoDePrueba("Roll X", "Roll");

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(999L, nuevosDatos));
    }

    @Test
    @DisplayName("cambiarDisponibilidad activa y desactiva el plato correctamente")
    void cambiarDisponibilidad_debeActualizarEstado() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));

        service.cambiarDisponibilidad(creado.getId(), false);

        assertFalse(service.obtenerPorId(creado.getId()).estaDisponible());
    }

    @Test
    @DisplayName("cambiarDisponibilidad con id inexistente lanza RecursoNoEncontradoException")
    void cambiarDisponibilidad_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.cambiarDisponibilidad(999L, true));
    }

    @Test
    @DisplayName("eliminar con id existente elimina el plato")
    void eliminar_existente_debeEliminarPlato() {
        Plato creado = service.crear(platoDePrueba("Roll California", "Roll"));

        service.eliminar(creado.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }

    @Test
    @DisplayName("obtenerDisponibles filtra solo los platos disponibles")
    void obtenerDisponibles_debeFiltrarSoloDisponibles() {
        Plato disponible = service.crear(platoDePrueba("Roll California", "Roll"));
        Plato noDisponible = service.crear(platoDePrueba("Sashimi", "Sashimi"));
        service.cambiarDisponibilidad(noDisponible.getId(), false);

        List<Plato> resultado = service.obtenerDisponibles();

        assertEquals(1, resultado.size());
        assertEquals(disponible.getId(), resultado.get(0).getId());
    }

    @Test
    @DisplayName("obtenerPorCategoria filtra por categoria ignorando mayusculas")
    void obtenerPorCategoria_debeFiltrarPorCategoria() {
        service.crear(platoDePrueba("Roll California", "Roll"));
        service.crear(platoDePrueba("Sashimi mixto", "Sashimi"));

        List<Plato> resultado = service.obtenerPorCategoria("roll");

        assertEquals(1, resultado.size());
        assertEquals("Roll California", resultado.get(0).getNombre());
    }
}
