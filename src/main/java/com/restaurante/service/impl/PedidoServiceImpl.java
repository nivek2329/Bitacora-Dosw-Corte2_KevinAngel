package com.restaurante.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.EstadoPedido;
import com.restaurante.model.domain.Pedido;
import com.restaurante.service.IPedidoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PedidoServiceImpl implements IPedidoService {

    private final Map<Long, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public List<Pedido> obtenerTodos() {
        log.info("Obteniendo todos los pedidos. Total: {}", pedidos.size());
        return pedidos.values().stream().toList();
    }

    @Override
    public List<Pedido> obtenerPorMesa(Long idMesa) {
        return pedidos.values().stream()
                .filter(p -> p.getIdMesa().equals(idMesa))
                .toList();
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidos.values().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado: id={}", id);
                    return new RecursoNoEncontradoException("Pedido", id);
                });
    }

    @Override
    public Pedido crear(Pedido pedido) {
        pedido.setId(contador.getAndIncrement());
        pedidos.put(pedido.getId(), pedido);
        log.info("Pedido creado: id={}, idMesa={}", pedido.getId(), pedido.getIdMesa());
        return pedido;
    }

    @Override
    public Pedido actualizar(Long id, Pedido nuevosDatos) {
        Pedido existente = obtenerPorId(id);
        existente.setIdMesa(nuevosDatos.getIdMesa());
        log.info("Pedido actualizado: id={}", id);
        return existente;
    }

    @Override
    public Pedido cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(estado);
        log.info("Pedido id={} -> estado={}", id, estado);
        return pedido;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        pedidos.remove(id);
        log.info("Pedido eliminado: id={}", id);
    }
}
