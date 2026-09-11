import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona {
    private int historiaClinica;
    private List<Turno> turnos;

    public Paciente(String nombre, String apellido, String fechaNacimiento, String dni, int historiaClinica) {
        super(nombre, apellido, fechaNacimiento, dni);

        this.historiaClinica = historiaClinica;
        this.turnos = new ArrayList<>();
    }

    public void AgregarTurno(Turno nuevoTurno) {
        turnos.add(nuevoTurno);
    }
}
