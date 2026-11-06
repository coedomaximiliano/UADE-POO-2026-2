package ar.edu.uade.poo.grupo3.tpo.controlador;

import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import java.io.IOException;
import java.util.List;

/**
 * Controlador del ABM de sedes (patrón MVC): las vistas Swing le piden las operaciones,
 * él se las delega a la Boleteria (modelo) y guarda los cambios en datos/sedes.txt y datos/sectores.txt.
 */
public class SedeController {

    private final Boleteria boleteria;

    public SedeController(Boleteria boleteria) {
        this.boleteria = boleteria;
    }

    public List<Sede> listar(String filtro) {
        String f = filtro == null ? "" : filtro.trim().toLowerCase();
        return boleteria.getSedes().stream()
                .filter(s -> s.getNombre().toLowerCase().contains(f) || s.getDireccion().toLowerCase().contains(f))
                .toList();
    }

    public int funcionesActivas(Sede sede) {
        return boleteria.getFuncionesActivas(sede).size();
    }

    public Sede alta(String nombre, String direccion, List<Sector> sectores) throws IOException {
        Sede sede = new Sede(nombre, direccion);
        sectores.forEach(sede::agregarSector);
        boleteria.agregarSede(sede);
        boleteria.guardarSedes();
        return sede;
    }

    public void modificarDatos(Sede sede, String nombre, String direccion) throws IOException {
        boleteria.modificarSede(sede, nombre, direccion);
        boleteria.guardarSedes();
    }

    public void agregarSector(Sede sede, Sector sector) throws IOException {
        boleteria.agregarSectorASede(sede, sector);
        boleteria.guardarSedes();
    }

    public void quitarSector(Sede sede, String codigo) throws IOException {
        boleteria.quitarSectorDeSede(sede, codigo);
        boleteria.guardarSedes();
    }

    public void baja(Sede sede) throws IOException {
        boleteria.eliminarSede(sede);
        boleteria.guardarSedes();
    }
}
