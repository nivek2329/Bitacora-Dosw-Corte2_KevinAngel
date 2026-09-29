package com.restaurante.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.EstadoMesa;
import com.restaurante.model.domain.Mesa;

class MesaServiceImplTest {

    private MesaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MesaServiceImpl();
    }

    private Mesa mesaDePrueba(int numero, int capacidad) {
        return Mesa.builder().numero(numero).capacidad(capacidad).estado(EstadoMesa.LIBRE).build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<Mesa> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear asigna un id y guarda la mesa")
    void crear_debeAsignarIdYGuardar() {
        Mesa resultado = service.crear(mesaDePrueba(1, 4));

        assertNotNull(resultado.getId());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("obtenerPorId con id existente devuelve la mesa")
    void obtenerPorId_existente_debeRetornarMesa() {
        Mesa creada = service.crear(mesaDePrueba(2, 6));

        Mesa resultado = service.obtenerPorId(creada.getId());

        assertEquals(2, resultado.getNumero());
    }

    @Test
    @DisplayName("obtenerPorId con id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("actualizar con id existente modifica los campos de la mesa")
    void actualizar_existente_debeActualizarCampos() {
        Mesa creada = service.crear(mesaDePrueba(3, 4));

        Mesa resultado = service.actualizar(creada.getId(), mesaDePrueba(3, 8));

        assertEquals(8, resultado.getCapacidad());
    }

    @Test
    @DisplayName("actualizar con id inexistente lanza RecursoNoEncontradoException")
    void actualizar_inexistente_debeLanzarExcepcion() {
        Mesa nuevosDatos = mesaDePrueba(1, 4);

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(999L, nuevosDatos));
    }

    @Test
    @DisplayName("cambiarEstado ocupa y libera la mesa correctamente")
    void cambiarEstado_debeActualizarEstado() {
        Mesa creada = service.crear(mesaDePrueba(4, 4));

        service.cambiarEstado(creada.getId(), EstadoMesa.OCUPADA);

        assertEquals(EstadoMesa.OCUPADA, service.obtenerPorId(creada.getId()).getEstado());
    }

    @Test
    @DisplayName("cambiarEstado a RESERVADA tambien actualiza el estado")
    void cambiarEstado_reservada_debeActualizarEstado() {
        Mesa creada = service.crear(mesaDePrueba(5, 4));

        service.cambiarEstado(creada.getId(), EstadoMesa.RESERVADA);

        assertEquals(EstadoMesa.RESERVADA, service.obtenerPorId(creada.getId()).getEstado());
    }

    @Test
    @DisplayName("cambiarEstado con id inexistente lanza RecursoNoEncontradoException")
    void cambiarEstado_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.cambiarEstado(999L, EstadoMesa.LIBRE));
    }

    @Test
    @DisplayName("eliminar con id existente elimina la mesa")
    void eliminar_existente_debeEliminarMesa() {
        Mesa creada = service.crear(mesaDePrueba(6, 4));

        service.eliminar(creada.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
