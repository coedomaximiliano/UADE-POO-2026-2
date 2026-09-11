import java.util.ArrayList;
import java.util.List;

public class Medico extends Persona {
    private String especialidad;
    private String matricula;
    private List<Turno> turnos;

    public Medico(String nombre, String apellido, String fechaNacimiento, String dni, String especialidad, String matricula) {
        super(nombre, apellido, fechaNacimiento, dni);

        this.especialidad = especialidad;
        this.matricula = matricula;
        this.turnos = new ArrayList<>();
    }



    public String getEspecialidad() {
        return especialidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void AgregarTurno(Turno nuevoTurno) {
        turnos.add(nuevoTurno);
    }
}
