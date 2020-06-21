package com.ceiba.biblioteca.dominio.servicio.libro;


import com.ceiba.biblioteca.dominio.Libro;
import com.ceiba.biblioteca.dominio.excepcion.LibroException;
import com.ceiba.biblioteca.dominio.repositorio.RepositorioLibro;
import org.springframework.stereotype.Component;

@Component
public class ServicioCrearLibro {

    private final RepositorioLibro repositorioLibro;
    public static final String EL_LIBRO_INGRESADO_YA_SE_ENCUENTRA_REGISTRADO = "El libro ingresado ya se encuentra registrado.";

    public ServicioCrearLibro(RepositorioLibro repositorioLibro) {
        this.repositorioLibro = repositorioLibro;
    }

    public void ejecutar(Libro libro) {
        if (this.repositorioLibro.obtenerPorIsbn(libro.getIsbn()) != null) {
            throw new LibroException(EL_LIBRO_INGRESADO_YA_SE_ENCUENTRA_REGISTRADO);
        } else {
            this.repositorioLibro.agregar(libro);
        }
    }
}
