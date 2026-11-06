package logica;

import modelo.Turno;

public interface AlmacenTurnos extends ConsultaTurnos {
    int getProximoId();

    void guardar(Turno turno);

    void actualizarEstado(Turno turno) throws TurnoException;

    void reasignar(Turno anterior, Turno nuevo) throws TurnoException;
}
