package com.restaurante.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.persistence.entity.PlatoEntity;

public interface PlatoJpaRepository extends JpaRepository<PlatoEntity, Long> {

    List<PlatoEntity> findByCategoria(String categoria);
    List<PlatoEntity> findByDisponible(Boolean disponible);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Long id);

}
