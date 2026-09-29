package com.restaurante.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.Cuenta;
import com.restaurante.service.ICuentaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CuentaServiceImpl implements ICuentaService {

    private final Map<Long, Cuenta> cuentas = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public List<Cuenta> obtenerTodos() {
        log.info("Obteniendo todas las cuentas. Total: {}", cuentas.size());
        return cuentas.values().stream().toList();
    }

    @Override
    public List<Cuenta> obtenerPorMesa(Long idMesa) {
        return cuentas.values().stream()
                .filter(c -> c.getIdMesa().equals(idMesa))
                .toList();
    }

    @Override
    public Cuenta obtenerPorId(Long id) {
        return cuentas.values().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Cuenta no encontrada: id={}", id);
                    return new RecursoNoEncontradoException("Cuenta", id);
                });
    }

    @Override
    public Cuenta crear(Cuenta cuenta) {
        cuenta.setId(contador.getAndIncrement());
        cuentas.put(cuenta.getId(), cuenta);
        log.info("Cuenta creada: id={}, idMesa={}", cuenta.getId(), cuenta.getIdMesa());
        return cuenta;
    }

    @Override
    public Cuenta actualizarTotal(Long id, Double total) {
        Cuenta existente = obtenerPorId(id);
        existente.setTotal(total);
        log.info("Cuenta id={} -> total={}", id, total);
        return existente;
    }

    @Override
    public Cuenta cerrar(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.cerrar();
        log.info("Cuenta cerrada: id={}", id);
        return cuenta;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        cuentas.remove(id);
        log.info("Cuenta eliminada: id={}", id);
    }
}
