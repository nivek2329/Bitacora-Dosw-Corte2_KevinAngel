package com.restaurante.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.RegistroVehiculo;
import com.restaurante.service.IRegistroVehiculoService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistroVehiculoServiceImpl implements IRegistroVehiculoService {

    private final Map<Long, RegistroVehiculo> registros = new ConcurrentHashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    @Override
    public List<RegistroVehiculo> obtenerTodos() {
        log.info("Obteniendo todos los registros de vehiculo. Total: {}", registros.size());
        return registros.values().stream().toList();
    }

    @Override
    public List<RegistroVehiculo> obtenerActivos() {
        return registros.values().stream()
                .filter(RegistroVehiculo::estaActivo)
                .toList();
    }

    @Override
    public RegistroVehiculo obtenerPorId(Long id) {
        return registros.values().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Registro de vehiculo no encontrado: id={}", id);
                    return new RecursoNoEncontradoException("RegistroVehiculo", id);
                });
    }

    @Override
    public RegistroVehiculo crear(RegistroVehiculo registro) {
        registro.setId(contador.getAndIncrement());
        registros.put(registro.getId(), registro);
        log.info("Registro de vehiculo creado: id={}, placa={}", registro.getId(), registro.getPlaca());
        return registro;
    }

    @Override
    public RegistroVehiculo registrarSalida(Long id) {
        RegistroVehiculo registro = obtenerPorId(id);
        registro.registrarSalida();
        log.info("Registro de vehiculo id={} -> salida registrada", id);
        return registro;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        registros.remove(id);
        log.info("Registro de vehiculo eliminado: id={}", id);
    }
}
