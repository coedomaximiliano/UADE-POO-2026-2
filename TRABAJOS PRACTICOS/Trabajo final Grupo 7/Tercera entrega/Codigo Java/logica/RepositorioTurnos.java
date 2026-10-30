package logica;

import modelo.EstadoTurno;
import modelo.Medico;
import modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RepositorioTurnos {
    // Por ahora queda en memoria. La conexion a la base se agrega despues.
    private ArrayList<Turno> turnos = new ArrayList<Turno>();
    private int proximoId = 1;

    public int getProximoId() {
        return proximoId;
    }

    public void guardar(Turno turno) {
        if (turno == null || buscarPorId(turno.getId()) != null) {
            throw new IllegalArgumentException("El turno es nulo o el ID ya existe.");
        }
        turnos.add(turno);
        proximoId = Math.max(proximoId, turno.getId() + 1);
    }

    public Turno buscarPorId(int id) {
        for (Turno turno : turnos) {
            if (turno.getId() == id) {
                return turno;
            }
        }
        return null;
    }

    public boolean disponible(Medico medico, LocalDate fecha, LocalTime hora) {
        for (Turno turno : turnos) {
            if (turno.getMedico().getMatricula().equals(medico.getMatricula())
                    && turno.getFecha().equals(fecha)
                    && turno.getHora().equals(hora)
                    && turno.getEstado() != EstadoTurno.CANCELADO) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Turno> consultar() {
        return new ArrayList<Turno>(turnos);
    }

    public ArrayList<Turno> consultarPorMedico(String matricula) {
        ArrayList<Turno> encontrados = new ArrayList<Turno>();
        for (Turno turno : turnos) {
            if (turno.getMedico().getMatricula().equals(matricula)) {
                encontrados.add(turno);
            }
        }
        return encontrados;
    }

    public ArrayList<Turno> consultarPorPaciente(String dni) {
        ArrayList<Turno> encontrados = new ArrayList<Turno>();
        for (Turno turno : turnos) {
            if (turno.getPaciente().getDni().equals(dni)) {
                encontrados.add(turno);
            }
        }
        return encontrados;
    }

    public ArrayList<Turno> consultarPorFecha(LocalDate fecha) {
        ArrayList<Turno> encontrados = new ArrayList<Turno>();
        for (Turno turno : turnos) {
            if (turno.getFecha().equals(fecha)) {
                encontrados.add(turno);
            }
        }
        return encontrados;
    }
}
