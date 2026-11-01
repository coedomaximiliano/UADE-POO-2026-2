package logica;

import modelo.Medico;
import modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// Para los servicios que solo necesitan consultar la agenda.
public interface ConsultaTurnos {
    Turno buscarPorId(int id);

    ArrayList<Turno> consultar();

    ArrayList<Turno> consultarPorMedico(String matricula);

    ArrayList<Turno> consultarPorPaciente(String dni);

    ArrayList<Turno> consultarPorFecha(LocalDate fecha);

    boolean disponible(Medico medico, LocalDate fecha, LocalTime hora);
}
