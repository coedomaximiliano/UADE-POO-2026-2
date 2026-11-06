package ar.edu.uade.poo.grupo3.tpo.modelo.excepcion;

/** La selección de ubicaciones no cumple una regla de venta (asiento ocupado, asiento suelto, preventa, etc.). */
public class SeleccionInvalidaException extends Exception {

    public SeleccionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
