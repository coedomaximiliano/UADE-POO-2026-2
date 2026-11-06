package ar.edu.uade.poo.grupo3.tpo.modelo.ventas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaqueteVIP {

    private final String nombre;
    private final double precio;
    private final List<String> beneficios;
    private int cupo;

    public PaqueteVIP(String nombre, double precio, int cupo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del paquete es obligatorio");
        }
        if (precio <= 0 || cupo < 0) {
            throw new IllegalArgumentException("Precio y cupo deben ser positivos");
        }
        this.nombre = nombre.trim();
        this.precio = precio;
        this.cupo = cupo;
        this.beneficios = new ArrayList<>();
    }

    public void agregarBeneficio(String beneficio) {
        if (beneficio != null && !beneficio.isBlank()) {
            beneficios.add(beneficio.trim());
        }
    }

    public boolean hayCupo() {
        return cupo > 0;
    }

    void reservarCupo() {
        if (!hayCupo()) {
            throw new IllegalStateException("No queda cupo para el paquete " + nombre);
        }
        cupo--;
    }

    void liberarCupo() {
        cupo++;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCupo() {
        return cupo;
    }

    public List<String> getBeneficios() {
        return Collections.unmodifiableList(beneficios);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
