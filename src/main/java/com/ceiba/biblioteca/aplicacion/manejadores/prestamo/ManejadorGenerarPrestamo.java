package com.ceiba.biblioteca.aplicacion.manejadores.prestamo;

import com.ceiba.biblioteca.dominio.servicio.bibliotecario.ServicioBibliotecario;
import com.ceiba.biblioteca.dominio.servicio.prestamo.ServicioObtenerPrestamo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ManejadorGenerarPrestamo {

    private final ServicioBibliotecario servicioBibliotecario;

    public ManejadorGenerarPrestamo(ServicioBibliotecario servicioBibliotecario) {
        this.servicioBibliotecario = servicioBibliotecario;
    }

    @Transactional
    public void ejecutar(String isbn, String nombreCliente) {
        this.servicioBibliotecario.prestar(isbn, nombreCliente);
    }
}
