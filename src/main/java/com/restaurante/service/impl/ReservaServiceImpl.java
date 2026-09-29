package com.restaurante.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.Reserva;
import com.restaurante.service.IReservaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReservaServiceImpl implements IReservaService {

    private final Map<Long, Reserva> reservas = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public List<Reserva> obtenerTodos() {
        log.info("Obteniendo todas las reservas. Total: {}", reservas.size());
        return reservas.values().stream().toList();
    }

    @Override
    public List<Reserva> obtenerPorMesa(Long idMesa) {
        return reservas.values().stream()
                .filter(r -> r.getIdMesa().equals(idMesa))
                .toList();
    }

    @Override
    public Reserva obtenerPorId(Long id) {
        return reservas.values().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Reserva no encontrada: id={}", id);
                    return new RecursoNoEncontradoException("Reserva", id);
                });
    }

    @Override
    public Reserva crear(Reserva reserva) {
        reserva.setId(contador.getAndIncrement());
        reservas.put(reserva.getId(), reserva);
        log.info("Reserva creada: id={}, idMesa={}", reserva.getId(), reserva.getIdMesa());
        return reserva;
    }

    @Override
    public Reserva actualizar(Long id, Reserva nuevosDatos) {
        Reserva existente = obtenerPorId(id);
        existente.setNombreCliente(nuevosDatos.getNombreCliente());
        existente.setFechaHora(nuevosDatos.getFechaHora());
        existente.setNumeroPersonas(nuevosDatos.getNumeroPersonas());
        log.info("Reserva actualizada: id={}", id);
        return existente;
    }

    @Override
    public Reserva cancelar(Long id) {
        Reserva reserva = obtenerPorId(id);
        reserva.cancelar();
        log.info("Reserva cancelada: id={}", id);
        return reserva;
    }

    @Override
    public Reserva reprogramar(Long id, LocalDateTime nuevaFechaHora) {
        Reserva reserva = obtenerPorId(id);
        reserva.reprogramar(nuevaFechaHora);
        log.info("Reserva id={} reprogramada a {}", id, nuevaFechaHora);
        return reserva;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        reservas.remove(id);
        log.info("Reserva eliminada: id={}", id);
    }
}
