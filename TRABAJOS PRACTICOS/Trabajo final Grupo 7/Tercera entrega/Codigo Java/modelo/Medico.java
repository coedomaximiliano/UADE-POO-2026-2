package modelo;

public class Medico extends Persona {
    private String matricula;
    private String especialidad;

    public Medico(String nombre, String dni, String email, String matricula, String especialidad) {
        super(nombre, dni, email);
        if (matricula == null || matricula.trim().isEmpty()
                || especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("Matricula y especialidad son obligatorias.");
        }
        this.matricula = matricula.trim();
        this.especialidad = especialidad.trim();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void actualizarEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        this.especialidad = especialidad.trim();
    }

    @Override
    public String mostrarDatos() {
        return getNombre() + " - Matricula: " + matricula + " - " + especialidad;
    }
}
