package com.ceiba.biblioteca.dominio.servicio.bibliotecario;

import com.ceiba.biblioteca.dominio.Libro;
import com.ceiba.biblioteca.dominio.Prestamo;
import com.ceiba.biblioteca.dominio.excepcion.PrestamoException;
import com.ceiba.biblioteca.dominio.repositorio.RepositorioLibro;
import com.ceiba.biblioteca.dominio.repositorio.RepositorioPrestamo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class ServicioBibliotecario {

    public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
    public static final String EL_LIBRO_NO_EXISTE = "El libro con el isbn ingresado no se encuentra registrado";
    public static final String EL_LIBRO_ES_PALINDROMO = "Los libros palíndromos solo se pueden utilizar en la biblioteca";
    public static final int NUMERO_DIAS_DEVOLUCION_MAXIMO = 14;

    private final RepositorioLibro repositorioLibro;
    private final RepositorioPrestamo repositorioPrestamo;

    public ServicioBibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioPrestamo = repositorioPrestamo;
    }

    public void prestar(String isbn, String nombreUsuario) {
        if (esLibro(isbn)) {
            if(esPrestado(isbn)) {
                throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
            } else if(esPalindromo(isbn)) {
                throw new PrestamoException(EL_LIBRO_ES_PALINDROMO);
            } else {
                Libro libro = repositorioLibro.obtenerPorIsbn(isbn);
                Prestamo prestamo = new Prestamo(libro);
                prestamo.setNombreUsuario(nombreUsuario);
                prestamo.setFechaEntregaMaxima(calcularFechaEntregaMaxima(isbn));
                this.repositorioPrestamo.agregar(prestamo);
            }
        } else {
            throw new PrestamoException(EL_LIBRO_NO_EXISTE);
        }
    }

    public boolean esPrestado(String isbn) {
        return this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn) != null ? true : false;
    }

    public boolean esLibro(String isbn) {
        return this.repositorioLibro.obtenerPorIsbn(isbn) != null ? true : false;
    }

    public boolean esPalindromo(String isbn) {

        isbn = isbn.toLowerCase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o")
                .replace("ú", "u").replace(" ", "").replace(".", "").replace(",", "");
        String cadenaInvertida = new StringBuilder(isbn).reverse().toString();

        return cadenaInvertida.equals(isbn);
    }

    public Date calcularFechaEntregaMaxima(String isbn) {
        char [] cadenaIsbn = isbn.toCharArray();
        int sumatoria = 0;
        for (char valor : cadenaIsbn) {
            if (Character.isDigit(valor))
                sumatoria += Integer.parseInt(String.valueOf(valor));
        }
        if (sumatoria > 30) {
            return calcularSumatoriaDiasFecha(NUMERO_DIAS_DEVOLUCION_MAXIMO);
        } else {
            return null;
        }
    }

    public Date calcularSumatoriaDiasFecha(int dias) {
        LocalDate fechaEntregaMaxima = LocalDate.now();
        int contadorDias = 0;
        while (contadorDias < dias) {
            fechaEntregaMaxima = fechaEntregaMaxima.plusDays(1);
            if (!(fechaEntregaMaxima.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++contadorDias;
            }
        }
        return Date.from(fechaEntregaMaxima.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
