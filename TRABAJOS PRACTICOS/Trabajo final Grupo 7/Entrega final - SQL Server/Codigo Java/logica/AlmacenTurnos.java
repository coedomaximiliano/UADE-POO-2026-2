package logica;

import modelo.Turno;

// Agrega las operaciones de escritura al contrato de consulta.
public interface AlmacenTurnos extends ConsultaTurnos {
    int getProximoId();

    void guardar(Turno turno);

    void actualizarEstado(Turno turno) throws TurnoException;

    void reasignar(Turno anterior, Turno nuevo) throws TurnoException;
}
