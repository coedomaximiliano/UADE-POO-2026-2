package ar.edu.uade.poo.grupo3.tpo.modelo.enums;

public enum EstadoEntrada {
    RESERVADA,
    PAGADA,
    ANULADA,
    DEVUELTA;

    /** Una entrada activa ocupa su lugar en la función. */
    public boolean estaActiva() {
        return this == RESERVADA || this == PAGADA;
    }
}
