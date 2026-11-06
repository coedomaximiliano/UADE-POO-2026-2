package aplicacion;

import logica.AlmacenTurnos;
import logica.GestorTurnos;
import logica.NotificadorEmail;
import logica.RegistroMedicos;
import logica.RegistroPacientes;
import logica.RepositorioTurnos;

public class AplicacionClinica {
    public static final String VERSION = "5.0";

    private RegistroPacientes pacientes = new RegistroPacientes();
    private RegistroMedicos medicos = new RegistroMedicos();
    private AlmacenTurnos repositorio = new RepositorioTurnos();
    private NotificadorEmail notificador = new NotificadorEmail();
    private GestorTurnos gestor = new GestorTurnos(pacientes, medicos, repositorio, notificador);

    public AplicacionClinica() {
        persistencia.ConexionSQL.comprobar();
    }

    public RegistroPacientes getPacientes() {
        return pacientes;
    }

    public RegistroMedicos getMedicos() {
        return medicos;
    }

    public GestorTurnos getGestor() {
        return gestor;
    }

    public String getAvisoCorreo() {
        return notificador.getUltimoMensaje();
    }

    public void eliminarPaciente(String dni) {
        if (!gestor.consultarPorPaciente(dni).isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: el paciente tiene historial de turnos.");
        }
        pacientes.eliminar(dni);
    }

    public void eliminarMedico(String matricula) {
        if (!gestor.consultarPorMedico(matricula).isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: el medico tiene historial de turnos.");
        }
        medicos.eliminar(matricula);
    }
}
