package logica;

public class TurnoException extends Exception {
    // Para avisar un problema del turno sin cerrar el programa.
    public TurnoException(String mensaje) {
        super(mensaje);
    }
}
