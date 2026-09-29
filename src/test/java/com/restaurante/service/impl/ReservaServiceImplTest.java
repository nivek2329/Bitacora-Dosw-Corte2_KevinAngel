package com.restaurante.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurante.exception.RecursoNoEncontradoException;
import com.restaurante.model.domain.Reserva;

class ReservaServiceImplTest {

    private ReservaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReservaServiceImpl();
    }

    private Reserva reservaDePrueba(Long idMesa, String cliente) {
        return Reserva.builder()
                .idMesa(idMesa)
                .nombreCliente(cliente)
                .fechaHora(LocalDateTime.now().plusDays(1))
                .numeroPersonas(2)
                .build();
    }

    @Test
    @DisplayName("obtenerTodos con la lista vacia devuelve lista vacia, no null")
    void obtenerTodos_listaVacia_debeRetornarListaVacia() {
        List<Reserva> resultado = service.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crear asigna un id y guarda la reserva")
    void crear_debeAsignarIdYGuardar() {
        Reserva resultado = service.crear(reservaDePrueba(1L, "Juan Perez"));

        assertNotNull(resultado.getId());
        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    @DisplayName("obtenerPorId con id inexistente lanza RecursoNoEncontradoException")
    void obtenerPorId_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(999L));
    }

    @Test
    @DisplayName("obtenerPorMesa filtra las reservas de esa mesa")
    void obtenerPorMesa_debeFiltrarPorMesa() {
        service.crear(reservaDePrueba(1L, "Cliente A"));
        service.crear(reservaDePrueba(2L, "Cliente B"));

        List<Reserva> resultado = service.obtenerPorMesa(2L);

        assertEquals(1, resultado.size());
        assertEquals("Cliente B", resultado.get(0).getNombreCliente());
    }

    @Test
    @DisplayName("actualizar modifica los campos de la reserva")
    void actualizar_existente_debeActualizarCampos() {
        Reserva creada = service.crear(reservaDePrueba(1L, "Cliente A"));
        Reserva nuevosDatos = reservaDePrueba(1L, "Cliente A Actualizado");
        nuevosDatos.setNumeroPersonas(5);

        Reserva resultado = service.actualizar(creada.getId(), nuevosDatos);

        assertEquals("Cliente A Actualizado", resultado.getNombreCliente());
        assertEquals(5, resultado.getNumeroPersonas());
    }

    @Test
    @DisplayName("actualizar con id inexistente lanza RecursoNoEncontradoException")
    void actualizar_inexistente_debeLanzarExcepcion() {
        Reserva nuevosDatos = reservaDePrueba(1L, "Cliente X");

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(999L, nuevosDatos));
    }

    @Test
    @DisplayName("cancelar marca la reserva como cancelada")
    void cancelar_debeMarcarComoCancelada() {
        Reserva creada = service.crear(reservaDePrueba(1L, "Cliente A"));

        service.cancelar(creada.getId());

        assertTrue(service.obtenerPorId(creada.getId()).isCancelada());
    }

    @Test
    @DisplayName("cancelar con id inexistente lanza RecursoNoEncontradoException")
    void cancelar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.cancelar(999L));
    }

    @Test
    @DisplayName("reprogramar cambia la fecha y hora de la reserva")
    void reprogramar_debeCambiarFechaHora() {
        Reserva creada = service.crear(reservaDePrueba(1L, "Cliente A"));
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(5);

        Reserva resultado = service.reprogramar(creada.getId(), nuevaFecha);

        assertEquals(nuevaFecha, resultado.getFechaHora());
    }

    @Test
    @DisplayName("reprogramar con id inexistente lanza RecursoNoEncontradoException")
    void reprogramar_inexistente_debeLanzarExcepcion() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(5);

        assertThrows(RecursoNoEncontradoException.class, () -> service.reprogramar(999L, nuevaFecha));
    }

    @Test
    @DisplayName("eliminar con id existente elimina la reserva")
    void eliminar_existente_debeEliminarReserva() {
        Reserva creada = service.crear(reservaDePrueba(1L, "Cliente A"));

        service.eliminar(creada.getId());

        assertTrue(service.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("eliminar con id inexistente lanza RecursoNoEncontradoException")
    void eliminar_inexistente_debeLanzarExcepcion() {
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(999L));
    }
}
