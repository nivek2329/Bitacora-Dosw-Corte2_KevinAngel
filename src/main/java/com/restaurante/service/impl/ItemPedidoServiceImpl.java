package com.restaurante.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.ItemPedido;
import com.restaurante.model.domain.Pedido;
import com.restaurante.model.domain.Plato;
import com.restaurante.service.IItemPedidoService;
import com.restaurante.service.IPedidoService;
import com.restaurante.service.IPlatoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemPedidoServiceImpl implements IItemPedidoService {

    private final Map<Long, ItemPedido> items = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    private final IPedidoService pedidoService;
    private final IPlatoService platoService;

    @Override
    public List<ItemPedido> obtenerTodos() {
        log.info("Obteniendo todos los items de pedido. Total: {}", items.size());
        return items.values().stream().toList();
    }

    @Override
    public List<ItemPedido> obtenerPorPedido(Long idPedido) {
        return items.values().stream()
                .filter(i -> i.getIdPedido().equals(idPedido))
                .toList();
    }

    @Override
    public ItemPedido obtenerPorId(Long id) {
        return items.values().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Item de pedido no encontrado: id={}", id);
                    return new RecursoNoEncontradoException("ItemPedido", id);
                });
    }

    @Override
    public ItemPedido crear(ItemPedido item) {
        Pedido pedido = pedidoService.obtenerPorId(item.getIdPedido());
        Plato plato = platoService.obtenerPorId(item.getIdPlato());

        item.setId(contador.getAndIncrement());
        item.setNombrePlato(plato.getNombre());
        item.setPrecioUnitario(plato.getPrecio());

        items.put(item.getId(), item);
        pedido.agregarItem(item);
        log.info("Item de pedido creado: id={}, idPedido={}, idPlato={}", item.getId(), item.getIdPedido(), item.getIdPlato());
        return item;
    }

    @Override
    public ItemPedido actualizarCantidad(Long id, Integer cantidad) {
        ItemPedido item = obtenerPorId(id);
        item.setCantidad(cantidad);
        log.info("Item de pedido id={} -> cantidad={}", id, cantidad);
        return item;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        items.remove(id);
        log.info("Item de pedido eliminado: id={}", id);
    }
}
