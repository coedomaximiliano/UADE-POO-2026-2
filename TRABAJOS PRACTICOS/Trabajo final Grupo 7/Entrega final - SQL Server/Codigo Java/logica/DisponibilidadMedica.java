package logica;

import modelo.*;

import java.time.*;
import java.util.*;

public class DisponibilidadMedica {
    private RegistroMedicos medicos;
    private RepositorioTurnos turnos;

    public DisponibilidadMedica(RegistroMedicos medicos, RepositorioTurnos turnos) {
        this.medicos = medicos;
        this.turnos = turnos;
    }

    public LinkedHashMap<LocalDate, ArrayList<LocalTime>> consultar(
            String matricula, String dni, LocalDate desde, int dias) {
        return consultar(matricula, dni, desde, dias, -1);
    }

    public LinkedHashMap<LocalDate, ArrayList<LocalTime>> consultar(
            String matricula, String dni, LocalDate desde, int dias, int ignorarTurno) {
        LinkedHashMap<LocalDate, ArrayList<LocalTime>> disponibles =
                new LinkedHashMap<LocalDate, ArrayList<LocalTime>>();
        Medico medico = medicos.buscarPorMatricula(matricula);
        if (medico == null) return disponibles;
        if (desde == null || dias < 1 || dias > 366)
            throw new IllegalArgumentException("Revisar el período de consulta.");
        HashSet<String> ocupados = new HashSet<String>();
        // Al reasignar no contamos el turno que estamos moviendo como un horario ocupado.
        for (Turno t : turnos.consultar()) {
            if (t.getId() != ignorarTurno
                    && t.getEstado() != EstadoTurno.CANCELADO
                    && (t.getMedico().getMatricula().equals(matricula)
                            || t.getPaciente().getDni().equals(dni)))
                ocupados.add(t.getFecha() + "|" + t.getHora());
        }
        LocalDateTime ahora = LocalDateTime.now();
        ArrayList<AusenciaMedica> ausencias = new RegistroAusencias().listar();
        // Un día con ausencia queda afuera aunque coincida con la jornada habitual.
        for (int i = 0; i < dias; i++) {
            LocalDate fecha = desde.plusDays(i);
            ArrayList<LocalTime> horas = new ArrayList<LocalTime>();
            boolean ausente = false;
            for (AusenciaMedica a : ausencias)
                if (a.getMatricula().equals(matricula) && a.incluye(fecha)) {
                    ausente = true;
                    break;
                }
            if (ausente) continue;
            for (HorarioAtencion h : medico.getHorarios()) {
                if (h.getDia() != fecha.getDayOfWeek().getValue()) continue;
                for (LocalTime hora = h.getDesde();
                        hora.isBefore(h.getHasta());
                        hora = hora.plusMinutes(30)) {
                    if (LocalDateTime.of(fecha, hora).isAfter(ahora)
                            && !ocupados.contains(fecha + "|" + hora)) horas.add(hora);
                }
            }
            if (!horas.isEmpty()) disponibles.put(fecha, horas);
        }
        return disponibles;
    }
}
