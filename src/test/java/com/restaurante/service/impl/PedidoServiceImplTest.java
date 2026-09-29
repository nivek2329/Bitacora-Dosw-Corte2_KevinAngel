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
import com.restaurante.model.domain.EstadoPedido;
import com.restaurante.model.domain.Pedido;

class PedidoServiceImplTest {

    private PedidoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PedidoServiceImpl();
    }

    private Pedido pedidoDePrueba(Long idMesa) {
        return Pedido.builder().idMesa(idMesa).estado(EstadoPedido.RECIBIDO).build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<Pedido> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear asigna un id y guarda el pedido")
    void crear_debeAsignarIdYGuardar() {
        Pedido resultado = service.crear(pedidoDePrueba(1L));

        assertNotNull(resultado.getId());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("obtenerPorId con id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerPorMesa filtra los pedidos de esa mesa")
    void obtenerPorMesa_debeFiltrarPorMesa() {
        service.crear(pedidoDePrueba(1L));
        service.crear(pedidoDePrueba(2L));

        List<Pedido> resultado = service.obtenerPorMesa(2L);

        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdMesa());
    }

    @Test
    @DisplayName("actualizar cambia la mesa asociada al pedido")
    void actualizar_existente_debeActualizarCampos() {
        Pedido creado = service.crear(pedidoDePrueba(1L));

        Pedido resultado = service.actualizar(creado.getId(), pedidoDePrueba(2L));

        assertEquals(2L, resultado.getIdMesa());
    }

    @Test
    @DisplayName("actualizar con id inexistente lanza RecursoNoEncontradoException")
    void actualizar_inexistente_debeLanzarExcepcion() {
        Pedido nuevosDatos = pedidoDePrueba(1L);

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(999L, nuevosDatos));
    }

    @Test
    @DisplayName("cambiarEstado actualiza el estado del pedido")
    void cambiarEstado_debeActualizarEstado() {
        Pedido creado = service.crear(pedidoDePrueba(1L));

        service.cambiarEstado(creado.getId(), EstadoPedido.EN_PREPARACION);

        assertEquals(EstadoPedido.EN_PREPARACION, service.obtenerPorId(creado.getId()).getEstado());
    }

    @Test
    @DisplayName("cambiarEstado con id inexistente lanza RecursoNoEncontradoException")
    void cambiarEstado_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.cambiarEstado(999L, EstadoPedido.LISTO));
    }

    @Test
    @DisplayName("eliminar con id existente elimina el pedido")
    void eliminar_existente_debeEliminarPedido() {
        Pedido creado = service.crear(pedidoDePrueba(1L));

        service.eliminar(creado.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
