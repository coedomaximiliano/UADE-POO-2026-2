package ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        if (buscarSector(sector.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un sector con código " + sector.getCodigo());
        }
        sectores.add(sector);
    }

    public boolean quitarSector(String codigo) {
        return sectores.removeIf(s -> s.getCodigo().equalsIgnoreCase(codigo));
    }

    public Optional<Sector> buscarSector(String codigo) {
        return sectores.stream().filter(s -> s.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public List<Sector> getSectores() {
        return Collections.unmodifiableList(sectores);
    }

    public int getCapacidadTotal() {
        return sectores.stream().mapToInt(Sector::getCapacidad).sum();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
