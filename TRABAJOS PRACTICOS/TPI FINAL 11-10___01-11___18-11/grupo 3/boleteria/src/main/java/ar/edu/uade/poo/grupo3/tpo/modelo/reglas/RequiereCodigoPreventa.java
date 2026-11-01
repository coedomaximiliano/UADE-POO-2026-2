package ar.edu.uade.poo.grupo3.tpo.modelo.reglas;

import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;

import java.time.LocalDateTime;
import java.util.List;

/** Mientras la función está en preventa, solo se compra con el código de acceso. */
public class RequiereCodigoPreventa implements ReglaDeVenta {

    @Override
    public void validar(Compra compra, Funcion funcion, Sector sector, List<Asiento> seleccion,
                        LocalDateTime ahora) throws SeleccionInvalidaException {
        if (funcion.estaEnPreventa(ahora) && !funcion.getPreventa().validarCodigo(compra.getCodigoPreventa())) {
            throw new SeleccionInvalidaException(
                    "La función está en preventa: ingresá un código de acceso válido.");
        }
    }
}
