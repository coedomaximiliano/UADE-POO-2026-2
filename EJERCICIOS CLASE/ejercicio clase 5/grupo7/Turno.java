import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    private Medico medico;
    private Paciente paciente;

    public Turno(LocalDate fecha, LocalTime hora, Medico medico, Paciente paciente) {
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoTurno.PENDIENTE;
        this.medico = medico;
        this.paciente = paciente;

        medico.agregarTurno(this);
        paciente.agregarTurno(this);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void atender() {
        estado = EstadoTurno.ATENDIDO;
    }

    public void cancelar() {
        estado = EstadoTurno.CANCELADO;
    }

    public void mostrarDetalle() {
        System.out.println("Turno: " + fecha + " " + hora);
        System.out.println("Estado: " + estado);
        System.out.println("Medico: " + medico.getNombre() + " - " + medico.getEspecialidad());
        System.out.println("Paciente: " + paciente.getNombre()
                + " - HC " + paciente.getNumeroHistoriaClinica());
    }
}
