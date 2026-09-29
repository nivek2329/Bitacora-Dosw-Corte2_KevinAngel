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
import com.restaurante.model.domain.ItemPedido;
import com.restaurante.model.domain.Pedido;
import com.restaurante.model.domain.Plato;

class ItemPedidoServiceImplTest {

    private ItemPedidoServiceImpl service;
    private PedidoServiceImpl pedidoService;
    private PlatoServiceImpl platoService;

    private Long idPedido;
    private Long idPlato;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoServiceImpl();
        platoService = new PlatoServiceImpl();
        service = new ItemPedidoServiceImpl(pedidoService, platoService);

        Pedido pedido = pedidoService.crear(Pedido.builder().idMesa(1L).build());
        idPedido = pedido.getId();

        Plato plato = platoService.crear(Plato.builder()
                .nombre("Roll California")
                .precio(25000.0)
                .categoria("Roll")
                .disponible(true)
                .build());
        idPlato = plato.getId();
    }

    private ItemPedido itemDePrueba(int cantidad) {
        return ItemPedido.builder().idPedido(idPedido).idPlato(idPlato).cantidad(cantidad).build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<ItemPedido> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear congela el nombre y el precio del plato, y lo agrega al pedido")
    void crear_debeCongelarNombreYPrecioYAgregarAlPedido() {
        ItemPedido resultado = service.crear(itemDePrueba(2));

        assertNotNull(resultado.getId());
        assertEquals("Roll California", resultado.getNombrePlato());
        assertEquals(25000.0, resultado.getPrecioUnitario());
        assertEquals(1, pedidoService.obtenerPorId(idPedido).getItems().size());
    }

    @Test
    @DisplayName("crear con idPedido inexistente lanza RecursoNoEncontradoException")
    void crear_pedidoInexistente_debeLanzarExcepcion() {
        ItemPedido item = ItemPedido.builder().idPedido(999L).idPlato(idPlato).cantidad(1).build();

        assertThrows(RecursoNoEncontradoException.class, () -> service.crear(item));
    }

    @Test
    @DisplayName("crear con idPlato inexistente lanza RecursoNoEncontradoException")
    void crear_platoInexistente_debeLanzarExcepcion() {
        ItemPedido item = ItemPedido.builder().idPedido(idPedido).idPlato(999L).cantidad(1).build();

        assertThrows(RecursoNoEncontradoException.class, () -> service.crear(item));
    }

    @Test
    @DisplayName("obtenerPorPedido filtra los items de ese pedido")
    void obtenerPorPedido_debeFiltrarPorPedido() {
        service.crear(itemDePrueba(1));

        List<ItemPedido> resultado = service.obtenerPorPedido(idPedido);

        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("actualizarCantidad cambia la cantidad del item")
    void actualizarCantidad_debeActualizarCantidad() {
        ItemPedido creado = service.crear(itemDePrueba(1));

        ItemPedido resultado = service.actualizarCantidad(creado.getId(), 4);

        assertEquals(4, resultado.getCantidad());
    }

    @Test
    @DisplayName("actualizarCantidad con id inexistente lanza RecursoNoEncontradoException")
    void actualizarCantidad_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarCantidad(999L, 2));
    }

    @Test
    @DisplayName("eliminar con id existente elimina el item")
    void eliminar_existente_debeEliminarItem() {
        ItemPedido creado = service.crear(itemDePrueba(1));

        service.eliminar(creado.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
