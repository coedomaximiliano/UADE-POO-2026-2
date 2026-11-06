package ar.edu.uade.poo.grupo3.tpo.modelo.enums;

/** Ciclo de vida de una compra: se reserva por 5 minutos y luego se confirma, vence o se cancela. */
public enum EstadoCompra {
    RESERVADA,
    CONFIRMADA,
    VENCIDA,
    CANCELADA
}
