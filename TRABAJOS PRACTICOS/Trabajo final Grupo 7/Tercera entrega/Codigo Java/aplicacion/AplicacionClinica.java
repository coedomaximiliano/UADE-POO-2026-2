package aplicacion;

import logica.GestorTurnos;
import logica.NotificadorEmail;
import logica.RegistroMedicos;
import logica.RegistroPacientes;
import logica.RepositorioTurnos;
import modelo.Medico;
import modelo.Paciente;

public class AplicacionClinica {
    private RegistroPacientes pacientes = new RegistroPacientes();
    private RegistroMedicos medicos = new RegistroMedicos();
    private RepositorioTurnos repositorio = new RepositorioTurnos();
    private NotificadorEmail notificador = new NotificadorEmail();
    private GestorTurnos gestor = new GestorTurnos(pacientes, medicos, repositorio, notificador);

    public AplicacionClinica() {
        pacientes.agregar(new Paciente("Ana Perez", "30111222", "ana@example.com", "HC001"));
        pacientes.agregar(new Paciente("Juan Diaz", "32444555", "juan@example.com", "HC002"));
        medicos.agregar(new Medico("Laura Gomez", "25111222", "laura@example.com", "MP100", "Clinica medica"));
        medicos.agregar(new Medico("Pedro Ruiz", "26333444", "pedro@example.com", "MP200", "Cardiologia"));
    }

    public RegistroPacientes getPacientes() { return pacientes; }
    public RegistroMedicos getMedicos() { return medicos; }
    public GestorTurnos getGestor() { return gestor; }
    public String getAvisoCorreo() { return notificador.getUltimoMensaje(); }

    public void eliminarPaciente(String dni) {
        if (!gestor.consultarPorPaciente(dni).isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar: el paciente tiene historial de turnos.");
        }
        pacientes.eliminar(dni);
    }

    public void eliminarMedico(String matricula) {
        if (!gestor.consultarPorMedico(matricula).isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar: el medico tiene historial de turnos.");
        }
        medicos.eliminar(matricula);
    }
}
