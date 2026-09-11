package eventos;

import java.util.ArrayList;
import java.util.List;

/**
 * AGREGACION: CentroDeEventos solo referencia Salones ya existentes; un
 * salon podria transferirse a otro centro de la cadena sin dejar de existir.
 */
public class CentroDeEventos {

    private String nombre;
    private String direccion;
    private List<Salon> salones = new ArrayList<>();

    public CentroDeEventos(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public void agregarSalon(Salon s) {
        salones.add(s);
    }

    public void quitarSalon(Salon s) {
        salones.remove(s);
    }

    public List<Salon> getSalones() {
        return salones;
    }
}
