package logica;

import modelo.Medico;
import modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public interface ConsultaTurnos {
    Turno buscarPorId(int id);

    ArrayList<Turno> consultar();

    ArrayList<Turno> consultarPorMedico(String matricula);

    ArrayList<Turno> consultarPorPaciente(String dni);

    ArrayList<Turno> consultarPorFecha(LocalDate fecha);

    boolean disponible(Medico medico, LocalDate fecha, LocalTime hora);
}
