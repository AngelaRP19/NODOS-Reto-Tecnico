package com.nodo.retotecnico.exception;

import java.util.Map;

/**
 * Errores de validación por campo detectados fuera de @Valid (ej. unicidad de
 * username/email chequeada a mano en el service). GlobalExceptionHandler la
 * traduce al mismo formato 400 + {"campo":"mensaje"} que ya usa
 * MethodArgumentNotValidException, para que el frontend no tenga que
 * distinguir el origen del error.
 */
public class FieldValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public FieldValidationException(Map<String, String> errors) {
        super("Errores de validación: " + errors);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
