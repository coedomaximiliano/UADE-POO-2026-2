package ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura;

import java.util.Objects;

/** Asiento físico de un sector numerado. No guarda si está vendido: eso lo sabe cada Funcion. */
public class Asiento {

    private final String fila;
    private final int numero;

    public Asiento(String fila, int numero) {
        if (fila == null || fila.isBlank()) {
            throw new IllegalArgumentException("La fila no puede estar vacía");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de asiento debe ser positivo");
        }
        this.fila = fila.trim().toUpperCase();
        this.numero = numero;
    }

    public String getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    /** Dos asientos son adyacentes si están en la misma fila y sus números difieren en uno. */
    public boolean esAdyacente(Asiento otro) {
        return otro != null && fila.equals(otro.fila) && Math.abs(numero - otro.numero) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asiento otro)) {
            return false;
        }
        return numero == otro.numero && fila.equals(otro.fila);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, numero);
    }

    @Override
    public String toString() {
        return fila + "-" + numero;
    }
}
