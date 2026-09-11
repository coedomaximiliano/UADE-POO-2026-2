package consultorio;

import java.time.LocalDate;

/**
 * Turno asocia un Medico (1 a 0..*) y un Paciente (1 a 0..*): cada turno
 * referencia a exactamente un medico y un paciente, y cada uno de ellos
 * puede tener muchos turnos a lo largo del tiempo.
 */
public class Turno {

    private LocalDate fecha;
    private String hora;
    private String estado; // "PENDIENTE" | "ATENDIDO" | "CANCELADO"
    private Medico medico;
    private Paciente paciente;

    public Turno(LocalDate fecha, String hora, Medico medico, Paciente paciente) {
        this.fecha = fecha;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
        this.estado = "PENDIENTE";
    }

    public void atender() {
        this.estado = "ATENDIDO";
    }

    public void cancelar() {
        this.estado = "CANCELADO";
    }

    public String getEstado() {
        return estado;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
