import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paciente {
    private String nombre;
    private String numeroHistoriaClinica;
    private List<Turno> turnos;

    public Paciente(String nombre, String numeroHistoriaClinica) {
        this.nombre = nombre;
        this.numeroHistoriaClinica = numeroHistoriaClinica;
        this.turnos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public List<Turno> getTurnos() {
        return Collections.unmodifiableList(turnos);
    }

    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }
}
