package ar.edu.uade.poo.grupo3.tpo.modelo.excepcion;

/** Se intentó confirmar una compra cuya reserva de 5 minutos ya venció. */
public class ReservaVencidaException extends Exception {

    public ReservaVencidaException(String mensaje) {
        super(mensaje);
    }
}
