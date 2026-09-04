import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Medico {
    private String nombre;
    private String especialidad;
    private List<Turno> turnos;

    public Medico(String nombre, String especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.turnos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public List<Turno> getTurnos() {
        return Collections.unmodifiableList(turnos);
    }

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }
}
