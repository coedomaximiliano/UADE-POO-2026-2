package ar.edu.uade.poo.grupo3.tpo.modelo.excepcion;

/** La selección de ubicaciones no es válida (asiento ocupado, cantidad inválida, etc.). */
public class SeleccionInvalidaException extends Exception {

    public SeleccionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
