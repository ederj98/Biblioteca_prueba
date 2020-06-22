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
    public static final int NUMERO_DIAS_DEVOLUCION_MAXIMO = 15;
    public static final int SUMATORIA_MAXIMA_DIGITOS_NUMERICOS_ISBN = 30;

    private final RepositorioLibro repositorioLibro;
    private final RepositorioPrestamo repositorioPrestamo;

    public ServicioBibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioPrestamo = repositorioPrestamo;
    }

    /**
     * Metodo que permite realizar el prestamo de un libro
     * @param isbn
     * @param nombreUsuario
     */
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

    /**
     * Metodo que valida si un libro ya ha sido prestado.
     * @param isbn
     * @return boolean
     */
    public boolean esPrestado(String isbn) {
        return this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn) != null ? true : false;
    }

    /**
     * Metodo que valida si el libro con el isbn ingresado se encuentra registrado.
     * @param isbn
     * @return boolean
     */
    public boolean esLibro(String isbn) {
        return this.repositorioLibro.obtenerPorIsbn(isbn) != null ? true : false;
    }

    /**
     * Metodo que valida si un isbn es o no palindromo.
     * @param isbn
     * @return boolean
     */
    public boolean esPalindromo(String isbn) {
        String cadenaInvertida = new StringBuilder(isbn).reverse().toString();

        return cadenaInvertida.equals(isbn);
    }

    /**
     * Metodo que permite calcular la fecha de entrega maxima para devolver un libro
     * una vez prestado si la sumatoria de los digitos que componen al isbn suministrado
     * es mayor a SUMATORIA_MAXIMA_DIGITOS_NUMERICOS_ISBN
     * @param isbn
     * @return Date
     */
    public Date calcularFechaEntregaMaxima(String isbn) {
        char [] cadenaIsbn = isbn.toCharArray();
        int sumatoria = 0;
        for (char valor : cadenaIsbn) {
            if (Character.isDigit(valor))
                sumatoria += Integer.parseInt(String.valueOf(valor));
        }
        if (sumatoria > SUMATORIA_MAXIMA_DIGITOS_NUMERICOS_ISBN) {
            return calcularSumatoriaDiasFecha(NUMERO_DIAS_DEVOLUCION_MAXIMO);
        } else {
            return null;
        }
    }

    /**
     * Metodo que permite calcular la fecha de entrega maxima para devolver un libro
     * una vez prestado con base al parametro NUMERO_DIAS_DEVOLUCION_MAXIMO, omitiendo los domingos.
     * @param dias
     * @return Date
     */
    public Date calcularSumatoriaDiasFecha(int dias) {
        LocalDate fechaEntregaMaxima = LocalDate.now();
        if (fechaEntregaMaxima.getDayOfWeek() == DayOfWeek.SUNDAY) {
            fechaEntregaMaxima = fechaEntregaMaxima.plusDays(1);
        }

        int contadorDias = 1;
        while (contadorDias < dias) {
            fechaEntregaMaxima = fechaEntregaMaxima.plusDays(1);
            if (fechaEntregaMaxima.getDayOfWeek() != DayOfWeek.SUNDAY) {
                ++contadorDias;
            }
        }
        return Date.from(fechaEntregaMaxima.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
