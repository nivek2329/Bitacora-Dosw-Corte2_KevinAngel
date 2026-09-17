package com.restaurante.exception;

/**
 * Se lanza cuando se busca un recurso por id y no existe.
 * (El manejo uniforme con GlobalExceptionHandler + ErrorResponseDTO se
 * agrega en el paso de "Manejo de excepciones" de la guia.)
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super("No existe " + recurso + " con id=" + id);
    }
}
