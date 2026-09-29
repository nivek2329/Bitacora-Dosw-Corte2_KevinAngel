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
import com.restaurante.model.domain.Cuenta;
import com.restaurante.model.domain.EstadoCuenta;

class CuentaServiceImplTest {

    private CuentaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CuentaServiceImpl();
    }

    private Cuenta cuentaDePrueba(Long idMesa) {
        return Cuenta.builder().idMesa(idMesa).estado(EstadoCuenta.ABIERTA).total(0.0).build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<Cuenta> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear asigna un id y guarda la cuenta")
    void crear_debeAsignarIdYGuardar() {
        Cuenta resultado = service.crear(cuentaDePrueba(1L));

        assertNotNull(resultado.getId());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("obtenerPorId con id existente devuelve la cuenta")
    void obtenerPorId_existente_debeRetornarCuenta() {
        Cuenta creada = service.crear(cuentaDePrueba(2L));

        Cuenta resultado = service.obtenerPorId(creada.getId());

        assertEquals(2L, resultado.getIdMesa());
    }

    @Test
    @DisplayName("obtenerPorId con id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerPorMesa filtra las cuentas de esa mesa")
    void obtenerPorMesa_debeFiltrarPorMesa() {
        service.crear(cuentaDePrueba(1L));
        service.crear(cuentaDePrueba(2L));

        List<Cuenta> resultado = service.obtenerPorMesa(2L);

        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdMesa());
    }

    @Test
    @DisplayName("actualizarTotal cambia el total de la cuenta")
    void actualizarTotal_debeActualizarTotal() {
        Cuenta creada = service.crear(cuentaDePrueba(3L));

        Cuenta resultado = service.actualizarTotal(creada.getId(), 75000.0);

        assertEquals(75000.0, resultado.getTotal());
    }

    @Test
    @DisplayName("actualizarTotal con id inexistente lanza RecursoNoEncontradoException")
    void actualizarTotal_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarTotal(999L, 1000.0));
    }

    @Test
    @DisplayName("cerrar cambia el estado a CERRADA")
    void cerrar_debeCambiarEstado() {
        Cuenta creada = service.crear(cuentaDePrueba(4L));

        service.cerrar(creada.getId());

        assertEquals(EstadoCuenta.CERRADA, service.obtenerPorId(creada.getId()).getEstado());
    }

    @Test
    @DisplayName("cerrar con id inexistente lanza RecursoNoEncontradoException")
    void cerrar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.cerrar(999L));
    }

    @Test
    @DisplayName("eliminar con id existente elimina la cuenta")
    void eliminar_existente_debeEliminarCuenta() {
        Cuenta creada = service.crear(cuentaDePrueba(5L));

        service.eliminar(creada.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
