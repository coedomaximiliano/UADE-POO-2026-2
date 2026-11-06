package logica;

import modelo.EstadoTurno;
import modelo.Turno;

import java.time.*;

public class SeguimientoTurnos {
    public static final int MINUTOS_AVISO = 15;
    public static final int DURACION_TURNO = 30;

    public static String situacion(Turno turno, LocalDateTime ahora) {
        return situacion(turno.getEstado(), turno.getFecha(), turno.getHora(), ahora);
    }

    public static String situacion(
            EstadoTurno estado, LocalDate fecha, LocalTime hora, LocalDateTime ahora) {
        if (estado == EstadoTurno.ATENDIDO) return "Atendido";
        if (estado == EstadoTurno.CANCELADO) return "Cancelado";
        LocalDateTime inicio = LocalDateTime.of(fecha, hora);
        if (!ahora.isBefore(inicio.plusMinutes(DURACION_TURNO))) return "Vencido sin registrar";
        if (!ahora.isBefore(inicio)) return "En horario";
        if (!inicio.isAfter(ahora.plusMinutes(MINUTOS_AVISO))) return "Próximo";
        return "Programado";
    }

    public static boolean vencido(Turno t, LocalDateTime ahora) {
        return situacion(t, ahora).equals("Vencido sin registrar");
    }

    public static boolean proximo(Turno t, LocalDateTime ahora) {
        return situacion(t, ahora).equals("Próximo");
    }

    public static boolean enHorario(Turno t, LocalDateTime ahora) {
        return situacion(t, ahora).equals("En horario");
    }
}
