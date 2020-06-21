package com.ceiba.biblioteca.dominio.servicio.libro;

import com.ceiba.biblioteca.dominio.Libro;
import com.ceiba.biblioteca.dominio.excepcion.LibroException;
import com.ceiba.biblioteca.infraestructura.persistencia.repositorio.RepositorioLibroPersistente;
import org.springframework.stereotype.Component;

@Component
public class ServicioObtenerLibro {

    private final RepositorioLibroPersistente repositorioLibro;
    public static final String EL_LIBRO_CON_EL_ISBN_INGRESADO_NO_SE_ENCUENTRA_REGISTRADO = "El libro con el isbn ingresado no se encuentra registrado";

    public ServicioObtenerLibro(RepositorioLibroPersistente repositorioLibro) {
        this.repositorioLibro = repositorioLibro;
    }

    public Libro ejecutar(String isbn) {
        Libro libro = this.repositorioLibro.obtenerPorIsbn(isbn);
        if (libro != null) {
            return this.repositorioLibro.obtenerPorIsbn(isbn);
        } else {
            throw new LibroException(EL_LIBRO_CON_EL_ISBN_INGRESADO_NO_SE_ENCUENTRA_REGISTRADO);
        }
    }
}
