package logica;

import modelo.Medico;

import java.util.ArrayList;

public class RegistroMedicos {
    private ArrayList<Medico> medicos = new ArrayList<Medico>();

    public void agregar(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("Faltan los datos del medico.");
        }
        for (Medico actual : medicos) {
            if (actual.getMatricula().equals(medico.getMatricula())
                    || actual.getDni().equals(medico.getDni())) {
                throw new IllegalArgumentException("Ya existe ese medico o matricula.");
            }
        }
        medicos.add(medico);
    }

    public Medico buscarPorMatricula(String matricula) {
        for (Medico medico : medicos) {
            if (medico.getMatricula().equals(matricula)) {
                return medico;
            }
        }
        return null;
    }

    public ArrayList<Medico> listar() {
        return new ArrayList<Medico>(medicos);
    }

    public void modificar(Medico datos) {
        Medico actual = buscarPorMatricula(datos.getMatricula());
        if (actual == null || !actual.getDni().equals(datos.getDni())) {
            throw new IllegalArgumentException("No se puede cambiar el DNI ni la matricula.");
        }
        actual.actualizarDatos(datos.getNombre(), datos.getEmail());
        actual.actualizarEspecialidad(datos.getEspecialidad());
    }

    public void eliminar(String matricula) {
        Medico medico = buscarPorMatricula(matricula);
        if (medico == null) {
            throw new IllegalArgumentException("El medico ya no existe.");
        }
        medicos.remove(medico);
    }
}
