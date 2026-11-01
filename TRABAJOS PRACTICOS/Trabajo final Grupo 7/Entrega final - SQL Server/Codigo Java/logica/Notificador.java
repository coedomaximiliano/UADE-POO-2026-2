package logica;

import modelo.Turno;

public abstract class Notificador {
    public abstract boolean enviar(Turno turno);
}
