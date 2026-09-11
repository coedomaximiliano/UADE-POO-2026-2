package eventos;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSICION: Salon crea sus Sectores (new Sector(...) ocurre aqui
 * adentro). Sin el Salon, el Sector no tiene sentido.
 */
public class Salon {

    private String nombre;
    private int capacidad;
    private List<Sector> sectores = new ArrayList<>();

    public Salon(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public Sector agregarSector(String nombreSector, int capacidadSector) {
        Sector s = new Sector(nombreSector, capacidadSector);
        sectores.add(s);
        return s;
    }

    public List<Sector> getSectores() {
        return sectores;
    }

    public String getNombre() {
        return nombre;
    }
}
