package com.restaurante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurante.mapper.PlatoMapper;
import com.restaurante.model.domain.Plato;
import com.restaurante.model.dto.request.PlatoRequestDTO;
import com.restaurante.model.dto.response.PlatoResponseDTO;
import com.restaurante.service.IPlatoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Administracion de la carta (lo que ve/gestiona el gerente): crear,
 * editar, eliminar y activar/desactivar platos. El Controller no piensa:
 * recibe, traduce con el Mapper, delega al Service y responde.
 *
 * Para lo que ve el CLIENTE (solo el menu disponible) esta MenuController,
 * que reutiliza este mismo IPlatoService - ver discusion diapositiva 24.
 */
@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
@Slf4j
public class PlatoController {

    // El Mapper se inyecta AQUI - no en el Service
    private final IPlatoService platoService;
    private final PlatoMapper platoMapper;

    @GetMapping
    public ResponseEntity<List<PlatoResponseDTO>> obtenerTodos() {
        log.info("GET /api/platos");
        List<Plato> platos = platoService.obtenerTodos();
        return ResponseEntity.ok(platoMapper.toResponseList(platos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> obtenerPorId(@PathVariable Long id) {
        Plato plato = platoService.obtenerPorId(id);
        return ResponseEntity.ok(platoMapper.toResponse(plato));
    }

    @PostMapping
    public ResponseEntity<PlatoResponseDTO> crear(@RequestBody @Valid PlatoRequestDTO dto) {
        log.info("POST /api/platos - nombre={}", dto.getNombre());
        Plato plato = platoMapper.toDomain(dto); // MapperIn: RequestDTO -> dominio
        Plato creado = platoService.crear(plato); // Service recibe/devuelve dominio
        return ResponseEntity.status(HttpStatus.CREATED).body(platoMapper.toResponse(creado)); // MapperOut
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> actualizar(@PathVariable Long id,
                                                         @RequestBody @Valid PlatoRequestDTO dto) {
        Plato nuevosDatos = platoMapper.toDomain(dto);
        Plato actualizado = platoService.actualizar(id, nuevosDatos);
        return ResponseEntity.ok(platoMapper.toResponse(actualizado));
    }

    @PatchMapping("/{id}/disponible")
    public ResponseEntity<PlatoResponseDTO> cambiarDisponibilidad(@PathVariable Long id,
                                                                    @RequestParam boolean disponible) {
        Plato actualizado = platoService.cambiarDisponibilidad(id, disponible);
        return ResponseEntity.ok(platoMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204, sin body
    }
}
