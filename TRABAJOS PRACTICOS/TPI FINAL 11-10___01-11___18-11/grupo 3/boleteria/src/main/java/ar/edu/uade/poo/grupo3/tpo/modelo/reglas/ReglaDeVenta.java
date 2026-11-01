package ar.edu.uade.poo.grupo3.tpo.modelo.reglas;

import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Patrón Strategy: cada regla se valida antes de reservar. La Boleteria recorre la lista de reglas
 * sin saber cuáles son, así se pueden agregar reglas nuevas sin modificarla.
 */
public interface ReglaDeVenta {

    void validar(Compra compra, Funcion funcion, Sector sector, List<Asiento> seleccion, LocalDateTime ahora)
            throws SeleccionInvalidaException;
}
