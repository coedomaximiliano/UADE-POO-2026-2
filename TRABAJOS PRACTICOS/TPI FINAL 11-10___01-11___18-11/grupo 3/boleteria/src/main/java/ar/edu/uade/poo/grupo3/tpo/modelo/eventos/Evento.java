package ar.edu.uade.poo.grupo3.tpo.modelo.eventos;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoFuncion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Un espectáculo (recital, show) con sus funciones. Los artistas son nombres, sin repetidos. */
public class Evento {

    private final int id;
    private final String nombre;
    private final String descripcion;
    private final int duracionMinutos;
    private final String genero;
    private final List<String> artistas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();

    public Evento(int id, String nombre, String descripcion, int duracionMinutos, String genero) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser positiva");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.duracionMinutos = duracionMinutos;
        this.genero = genero == null ? "" : genero.trim();
    }

    public void agregarArtista(String artista) {
        if (artista == null || artista.isBlank()) {
            throw new IllegalArgumentException("El nombre del artista es obligatorio");
        }
        boolean repetido = artistas.stream().anyMatch(a -> a.equalsIgnoreCase(artista.trim()));
        if (!repetido) {
            artistas.add(artista.trim());
        }
    }

    public void agregarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función no puede ser nula");
        }
        funciones.add(funcion);
    }

    public List<Funcion> getFuncionesDisponibles() {
        return funciones.stream().filter(f -> f.getEstado() == EstadoFuncion.PROGRAMADA).toList();
    }

    public List<Funcion> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }

    public List<String> getArtistas() {
        return Collections.unmodifiableList(artistas);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getGenero() {
        return genero;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
