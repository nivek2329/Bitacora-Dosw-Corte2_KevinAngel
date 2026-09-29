package com.restaurante.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.RegistroVehiculo;

class RegistroVehiculoServiceImplTest {

    private RegistroVehiculoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RegistroVehiculoServiceImpl();
    }

    private RegistroVehiculo registroDePrueba(String placa) {
        return RegistroVehiculo.builder().placa(placa).horaEntrada(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<RegistroVehiculo> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear asigna un id y guarda el registro")
    void crear_debeAsignarIdYGuardar() {
        RegistroVehiculo resultado = service.crear(registroDePrueba("ABC123"));

        assertNotNull(resultado.getId());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("obtenerPorId con id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerActivos filtra solo los que no tienen salida registrada")
    void obtenerActivos_debeFiltrarSoloActivos() {
        RegistroVehiculo activo = service.crear(registroDePrueba("AAA111"));
        RegistroVehiculo salido = service.crear(registroDePrueba("BBB222"));
        service.registrarSalida(salido.getId());

        List<RegistroVehiculo> resultado = service.obtenerActivos();

        assertEquals(1, resultado.size());
        assertEquals(activo.getId(), resultado.get(0).getId());
    }

    @Test
    @DisplayName("registrarSalida asigna la hora de salida")
    void registrarSalida_debeAsignarHoraSalida() {
        RegistroVehiculo creado = service.crear(registroDePrueba("CCC333"));

        RegistroVehiculo resultado = service.registrarSalida(creado.getId());

        assertNotNull(resultado.getHoraSalida());
        assertFalse(resultado.estaActivo());
    }

    @Test
    @DisplayName("registrarSalida con id inexistente lanza RecursoNoEncontradoException")
    void registrarSalida_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.registrarSalida(999L));
    }

    @Test
    @DisplayName("eliminar con id existente elimina el registro")
    void eliminar_existente_debeEliminarRegistro() {
        RegistroVehiculo creado = service.crear(registroDePrueba("DDD444"));

        service.eliminar(creado.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
