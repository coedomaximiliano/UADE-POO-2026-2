package logica;

import modelo.Paciente;

import java.util.ArrayList;

public class RegistroPacientes {
    private ArrayList<Paciente> pacientes = new ArrayList<Paciente>();

    public void agregar(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Faltan los datos del paciente.");
        }
        for (Paciente actual : pacientes) {
            if (actual.getDni().equals(paciente.getDni())
                    || actual.getNumeroHistoriaClinica().equals(paciente.getNumeroHistoriaClinica())) {
                throw new IllegalArgumentException("Ya existe ese DNI o historia clinica.");
            }
        }
        pacientes.add(paciente);
    }

    public Paciente buscarPorDni(String dni) {
        for (Paciente paciente : pacientes) {
            if (paciente.getDni().equals(dni)) {
                return paciente;
            }
        }
        return null;
    }

    public ArrayList<Paciente> listar() {
        // Devuelvo una copia para no borrar la lista del registro desde afuera.
        return new ArrayList<Paciente>(pacientes);
    }

    public void modificar(Paciente datos) {
        Paciente actual = buscarPorDni(datos.getDni());
        if (actual == null) {
            throw new IllegalArgumentException("El paciente ya no existe.");
        }
        for (Paciente otro : pacientes) {
            if (otro != actual && otro.getNumeroHistoriaClinica().equals(datos.getNumeroHistoriaClinica())) {
                throw new IllegalArgumentException("Esa historia clinica pertenece a otro paciente.");
            }
        }
        // Modifico el mismo objeto porque puede tener turnos asociados.
        actual.actualizarDatos(datos.getNombre(), datos.getEmail());
        actual.actualizarHistoriaClinica(datos.getNumeroHistoriaClinica());
    }

    public void eliminar(String dni) {
        Paciente paciente = buscarPorDni(dni);
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente ya no existe.");
        }
        pacientes.remove(paciente);
    }
}
