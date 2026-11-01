package logica;

import modelo.EstadoTurno;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestorTurnos {
    private RegistroPacientes pacientes;
    private RegistroMedicos medicos;
    private RepositorioTurnos repositorio;
    private Notificador notificador;
    private ValidadorTurno validador;

    public GestorTurnos(
            RegistroPacientes pacientes,
            RegistroMedicos medicos,
            RepositorioTurnos repositorio,
            Notificador notificador) {
        this.pacientes = pacientes;
        this.medicos = medicos;
        this.repositorio = repositorio;
        this.notificador = notificador;
        this.validador = new ValidadorTurno();
    }

    public Turno cargarTurno(String dni, String matricula, LocalDate fecha, LocalTime hora)
            throws TurnoException {
        Paciente paciente = pacientes.buscarPorDni(dni);
        Medico medico = medicos.buscarPorMatricula(matricula);
        validador.validar(paciente, medico, fecha, hora);
        if (new RegistroAusencias().ausente(matricula, fecha))
            throw new TurnoException("El médico está ausente en esa fecha.");

        if (!repositorio.disponible(medico, fecha, hora)) {
            throw new TurnoException("El medico ya tiene un turno en ese horario.");
        }

        // Tambien evitamos que el paciente se reserve con dos medicos a la misma hora.
        for (Turno actual : repositorio.consultarPorPaciente(dni)) {
            if (actual.getFecha().equals(fecha)
                    && actual.getHora().equals(hora)
                    && actual.getEstado() != EstadoTurno.CANCELADO) {
                throw new TurnoException("El paciente ya tiene un turno en ese horario.");
            }
        }

        Turno turno = new Turno(repositorio.getProximoId(), fecha, hora, medico, paciente);
        repositorio.guardar(turno);
        System.out.println("Turno cargado con exito. Numero: " + turno.getId());

        // Primero guardo. Si falla el aviso no hay que cargar el turno otra vez.
        if (!notificador.enviar(turno)) {
            System.out.println("El turno sigue cargado, quedo pendiente el recordatorio.");
        }
        return turno;
    }

    public ArrayList<Turno> consultarTurnos() {
        return repositorio.consultar();
    }

    public ArrayList<Turno> consultarPorMedico(String matricula) {
        return repositorio.consultarPorMedico(matricula);
    }

    public ArrayList<Turno> consultarPorPaciente(String dni) {
        return repositorio.consultarPorPaciente(dni);
    }

    public ArrayList<Turno> consultarPorFecha(LocalDate fecha) {
        return repositorio.consultarPorFecha(fecha);
    }

    public void cancelarTurno(int id) throws TurnoException {
        Turno turno = buscarTurno(id);
        turno.cancelar();
        repositorio.actualizarEstado(turno);
    }

    public void atenderTurno(int id) throws TurnoException {
        Turno turno = buscarTurno(id);
        turno.atender();
        repositorio.actualizarEstado(turno);
    }

    public boolean enviarRecordatorio(int id) throws TurnoException {
        Turno turno = buscarTurno(id);
        if (turno.getEstado() != EstadoTurno.PENDIENTE) {
            throw new TurnoException("El recordatorio es solo para turnos pendientes.");
        }
        return notificador.enviar(turno);
    }

    public Turno consultarTurno(int id) throws TurnoException {
        return buscarTurno(id);
    }

    public Turno reasignarTurno(int id, String matricula, LocalDate fecha, LocalTime hora)
            throws TurnoException {
        Turno anterior = buscarTurno(id);
        if (anterior.getEstado() != EstadoTurno.PENDIENTE)
            throw new TurnoException("Solo se reasignan turnos pendientes.");
        Medico medico = medicos.buscarPorMatricula(matricula);
        validador.validar(anterior.getPaciente(), medico, fecha, hora);
        if (!anterior.getMedico().getEspecialidad().equalsIgnoreCase(medico.getEspecialidad()))
            throw new TurnoException("Elegí un médico de la misma especialidad.");
        if (new RegistroAusencias().ausente(matricula, fecha))
            throw new TurnoException("El médico está ausente en esa fecha.");
        Turno nuevo = new Turno(id, fecha, hora, medico, anterior.getPaciente());
        repositorio.reasignar(anterior, nuevo);
        return nuevo;
    }

    private Turno buscarTurno(int id) throws TurnoException {
        Turno turno = repositorio.buscarPorId(id);
        if (turno == null) {
            throw new TurnoException("No existe el turno " + id + ".");
        }
        return turno;
    }
}
