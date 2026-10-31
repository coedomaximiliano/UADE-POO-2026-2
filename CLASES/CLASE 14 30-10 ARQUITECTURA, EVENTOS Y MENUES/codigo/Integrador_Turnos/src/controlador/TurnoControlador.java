package controlador;

import datos.TurnoRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modelo.Turno;
import modelo.TurnoOcupadoException;

// GRASP Controlador: recibe los pedidos de la vista y coordina modelo + datos
public class TurnoControlador {
    private final TurnoRepositorio repo;
    private List<Turno> turnos = new ArrayList<>();

    public TurnoControlador(TurnoRepositorio repo) {
        this.repo = repo;
    }

    public void iniciar() throws IOException {
        turnos = repo.cargar();
    }

    public void reservar(Turno nuevo) throws TurnoOcupadoException {
        for (Turno t : turnos) {
            if (t.seSuperponeCon(nuevo)) {
                throw new TurnoOcupadoException("El Dr/a. " + nuevo.getMedico()
                        + " ya tiene turno el " + nuevo.getFecha() + " a las " + nuevo.getHora());
            }
        }
        turnos.add(nuevo);
    }

    public void cancelar(Turno t) {
        turnos.remove(t);
    }

    public List<Turno> listarOrdenados() {
        List<Turno> copia = new ArrayList<>(turnos);
        copia.sort(Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora));
        return copia;
    }

    public void guardar() throws IOException {
        repo.guardar(turnos);
    }
}
