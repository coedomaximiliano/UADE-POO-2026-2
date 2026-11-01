package logica;

import modelo.Turno;

public abstract class Notificador {
    // El gestor pide enviar, la clase hija se ocupa de como hacerlo.
    public abstract boolean enviar(Turno turno);
}
