package com.ceiba.biblioteca.dominio.servicio.prestamo;

import com.ceiba.biblioteca.dominio.Prestamo;
import com.ceiba.biblioteca.dominio.excepcion.PrestamoException;
import com.ceiba.biblioteca.dominio.repositorio.RepositorioPrestamo;
import org.springframework.stereotype.Component;

@Component
public class ServicioObtenerPrestamo {
    private final RepositorioPrestamo repositorioPrestamo;
    public static final String EL_LIBRO_CONSULTADO_NO_SE_ENCUENTRA_PRESTADO = "El libro consultado no se encuentra prestado.";

    public ServicioObtenerPrestamo(RepositorioPrestamo repositorioPrestamo) {
        this.repositorioPrestamo = repositorioPrestamo;
    }

    public Prestamo ejecutar(String isbn) {
        Prestamo prestamo = this.repositorioPrestamo.obtener(isbn);
        if (prestamo != null) {
            return prestamo;
        } else {
            throw new PrestamoException(EL_LIBRO_CONSULTADO_NO_SE_ENCUENTRA_PRESTADO);
        }
    }
}
