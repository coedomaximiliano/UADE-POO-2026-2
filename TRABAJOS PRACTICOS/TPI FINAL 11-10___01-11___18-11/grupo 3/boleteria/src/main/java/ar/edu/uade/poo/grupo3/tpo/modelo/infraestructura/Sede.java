package ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Lugar físico donde se hace el espectáculo (la Esfera, un estadio, un teatro). */
public class Sede {

    private String nombre;
    private String direccion;
    private final List<Sector> sectores;

    public Sede(String nombre, String direccion) {
        this.sectores = new ArrayList<>();
        setNombre(nombre);
        setDireccion(direccion);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la sede es obligatorio");
        }
        if (nombre.contains("|")) {
            throw new IllegalArgumentException("El nombre no puede contener '|'");
        }
        this.nombre = nombre.trim();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (direccion.contains("|")) {
            throw new IllegalArgumentException("La dirección no puede contener '|'");
        }
        this.direccion = direccion.trim();
    }

    public void agregarSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException("El sector no puede ser nulo");
        }
        if (buscarSector(sector.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un sector con código " + sector.getCodigo());
        }
        sectores.add(sector);
    }

    public boolean quitarSector(String codigo) {
        for (int i = 0; i < sectores.size(); i++) {
            if (sectores.get(i).getCodigo().equalsIgnoreCase(codigo)) {
                sectores.remove(i);
                return true;
            }
        }
        return false;
    }

    /** Devuelve null si no existe. */
    public Sector buscarSector(String codigo) {
        for (Sector s : sectores) {
            if (s.getCodigo().equalsIgnoreCase(codigo)) {
                return s;
            }
        }
        return null;
    }

    public List<Sector> getSectores() {
        return Collections.unmodifiableList(sectores);
    }

    public int getCapacidadTotal() {
        int total = 0;
        for (Sector s : sectores) {
            total += s.getCapacidad();
        }
        return total;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
