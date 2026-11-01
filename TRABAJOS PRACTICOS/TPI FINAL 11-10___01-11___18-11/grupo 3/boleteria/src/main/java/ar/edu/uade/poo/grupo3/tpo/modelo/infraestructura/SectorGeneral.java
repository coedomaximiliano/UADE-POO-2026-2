package ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.TipoSector;

/** Sector sin numerar (por ejemplo un campo de pie): solo importa la capacidad. */
public class SectorGeneral extends Sector {

    private final int capacidad;

    public SectorGeneral(String codigo, String nombre, int piso, double distanciaEscenario, int capacidad) {
        super(codigo, nombre, piso, distanciaEscenario);
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }
        this.capacidad = capacidad;
    }

    /** Polimorfismo: estar de pie es más barato que tener butaca en la misma ubicación. */
    @Override
    public double calcularMultiplicador() {
        return Math.round(super.calcularMultiplicador() * 0.8 * 100.0) / 100.0;
    }

    @Override
    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public TipoSector getTipo() {
        return TipoSector.GENERAL;
    }
}
