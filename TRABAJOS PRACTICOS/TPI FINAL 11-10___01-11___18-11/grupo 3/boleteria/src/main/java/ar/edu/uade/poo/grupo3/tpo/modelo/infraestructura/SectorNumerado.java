package ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.TipoSector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Sector con asientos numerados por fila (en la Esfera, todos los sectores son así). */
public class SectorNumerado extends Sector {

    private final List<Asiento> asientos;

    public SectorNumerado(String codigo, String nombre, int piso, double distanciaEscenario) {
        super(codigo, nombre, piso, distanciaEscenario);
        this.asientos = new ArrayList<>();
    }

    public void agregarAsiento(Asiento asiento) {
        if (asiento == null) {
            throw new IllegalArgumentException("El asiento no puede ser nulo");
        }
        if (asientos.contains(asiento)) {
            throw new IllegalArgumentException("El asiento " + asiento + " ya existe en el sector " + codigo);
        }
        asientos.add(asiento);
    }

    /** Genera filas A, B, C... con asientos numerados de 1 a asientosPorFila. */
    public void generarAsientos(int filas, int asientosPorFila) {
        if (filas <= 0 || filas > 26) {
            throw new IllegalArgumentException("La cantidad de filas debe estar entre 1 y 26");
        }
        if (asientosPorFila <= 0 || asientosPorFila > 100) {
            throw new IllegalArgumentException("Los asientos por fila deben estar entre 1 y 100");
        }
        for (int f = 0; f < filas; f++) {
            String fila = String.valueOf((char) ('A' + f));
            for (int n = 1; n <= asientosPorFila; n++) {
                agregarAsiento(new Asiento(fila, n));
            }
        }
    }

    public Optional<Asiento> buscarAsiento(String fila, int numero) {
        return asientos.stream()
                .filter(a -> a.getFila().equalsIgnoreCase(fila) && a.getNumero() == numero)
                .findFirst();
    }

    public List<Asiento> getAsientosDeFila(String fila) {
        return asientos.stream().filter(a -> a.getFila().equalsIgnoreCase(fila)).toList();
    }

    public List<String> getFilas() {
        return asientos.stream().map(Asiento::getFila).distinct().sorted().toList();
    }

    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    @Override
    public int getCapacidad() {
        return asientos.size();
    }

    @Override
    public TipoSector getTipo() {
        return TipoSector.NUMERADO;
    }
}
