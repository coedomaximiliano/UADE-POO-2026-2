package logica;

import modelo.Medico;
import modelo.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ValidadorTurno {
    public void validar(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora)
            throws TurnoException {
        if (paciente == null || medico == null) {
            throw new TurnoException("Primero hay que registrar al paciente y al medico.");
        }
        if (fecha == null || hora == null) {
            throw new TurnoException("Falta la fecha o la hora del turno.");
        }
        if (!LocalDateTime.of(fecha, hora).isAfter(LocalDateTime.now())) {
            throw new TurnoException("El turno tiene que ser para una fecha y hora futuras.");
        }
        // Usamos medias horas: 10:00, 10:30, 11:00, etc.
        if (hora.getMinute() % 30 != 0 || hora.getSecond() != 0 || hora.getNano() != 0) {
            throw new TurnoException("Los turnos empiezan en punto o a la media hora.");
        }
    }
}
