package modelo;

import logica.TurnoException;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    private Medico medico;
    private Paciente paciente;

    public Turno(int id, LocalDate fecha, LocalTime hora, Medico medico, Paciente paciente) {
        if (id <= 0 || fecha == null || hora == null || medico == null || paciente == null) {
            throw new IllegalArgumentException("El turno necesita todos sus datos.");
        }
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
        this.estado = EstadoTurno.PENDIENTE;
    }

    public int getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public EstadoTurno getEstado() { return estado; }
    public Medico getMedico() { return medico; }
    public Paciente getPaciente() { return paciente; }

    public void atender() throws TurnoException {
        if (estado != EstadoTurno.PENDIENTE) {
            throw new TurnoException("Solo se puede atender un turno pendiente.");
        }
        estado = EstadoTurno.ATENDIDO;
    }

    public void cancelar() throws TurnoException {
        // No borramos el turno, queda para poder consultarlo despues.
        if (estado != EstadoTurno.PENDIENTE) {
            throw new TurnoException("Solo se puede cancelar un turno pendiente.");
        }
        estado = EstadoTurno.CANCELADO;
    }

    public String mostrarDetalle() {
        return "Turno " + id + " | " + fecha + " " + hora
                + " | Paciente: " + paciente.getNombre()
                + " | Medico: " + medico.getNombre() + " | " + estado;
    }
}
