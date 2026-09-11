package consultorio;

public class Medico {

    private String matricula;
    private String nombre;
    private String apellido;
    private String especialidad;

    public Medico(String matricula, String nombre, String apellido, String especialidad) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
