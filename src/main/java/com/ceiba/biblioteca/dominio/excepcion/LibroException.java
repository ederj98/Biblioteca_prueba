package com.ceiba.biblioteca.dominio.excepcion;

public class LibroException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LibroException(String message) {
        super(message);
    }
}
