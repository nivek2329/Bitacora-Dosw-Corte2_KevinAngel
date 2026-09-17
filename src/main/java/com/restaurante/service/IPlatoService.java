package com.restaurante.service;

import java.util.List;

import com.restaurante.model.domain.Plato;

/**
 * Contrato del Service de Plato. Los metodos reciben y devuelven
 * objetos de DOMINIO - nunca DTOs. IPlatoService tambien es lo que
 * inyectaria MenuService/MenuController si necesitara reutilizar esta
 * logica (p. ej. obtenerDisponibles()) sin duplicarla.
 */
public interface IPlatoService {

    List<Plato> obtenerTodos();

    List<Plato> obtenerDisponibles();

    List<Plato> obtenerPorCategoria(String categoria);

    Plato obtenerPorId(Long id);

    Plato crear(Plato plato);

    Plato actualizar(Long id, Plato nuevosDatos);

    Plato cambiarDisponibilidad(Long id, boolean disponible);

    void eliminar(Long id);
}
