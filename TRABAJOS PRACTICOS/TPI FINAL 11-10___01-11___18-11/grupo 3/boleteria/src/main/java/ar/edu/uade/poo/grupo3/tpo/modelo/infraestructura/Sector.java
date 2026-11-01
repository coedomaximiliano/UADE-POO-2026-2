package ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.TipoSector;

import java.util.Objects;

/**
 * Zona de una sede. El precio de una entrada depende del piso y de la distancia al escenario
 * (como en la Esfera de Las Vegas): cuanto más cerca y más bajo, más caro.
 */
public abstract class Sector {

    protected final String codigo;
    protected String nombre;
    protected int piso;
    protected double distanciaEscenario;

    protected Sector(String codigo, String nombre, int piso, double distanciaEscenario) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del sector es obligatorio");
        }
        if (codigo.contains("|")) {
            throw new IllegalArgumentException("El código no puede contener '|'");
        }
        if (piso < 0) {
            throw new IllegalArgumentException("El piso no puede ser negativo");
        }
        if (distanciaEscenario < 0) {
            throw new IllegalArgumentException("La distancia al escenario no puede ser negativa");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.nombre = (nombre == null || nombre.isBlank()) ? this.codigo : nombre.trim();
        this.piso = piso;
        this.distanciaEscenario = distanciaEscenario;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPiso() {
        return piso;
    }

    public double getDistanciaEscenario() {
        return distanciaEscenario;
    }

    /** Multiplicador que se aplica sobre el precio base de la función. */
    public double calcularMultiplicador() {
        double porCercania = 1.0 + 1.0 / (1.0 + distanciaEscenario / 10.0);
        double porPiso = Math.max(0.6, 1.0 - piso * 0.1);
        return Math.round(porCercania * porPiso * 100.0) / 100.0;
    }

    public abstract int getCapacidad();

    public abstract TipoSector getTipo();

    /**
     * Factory: el único lugar donde se decide qué subclase crear según el tipo. Lo usan la pantalla
     * de sectores y el repositorio que lee los archivos, así nadie hace "new SectorNumerado" por su cuenta.
     *
     * @param filas            solo para sectores numerados
     * @param asientosPorFila  solo para sectores numerados
     * @param capacidad        solo para sectores generales
     */
    public static Sector crear(TipoSector tipo, String codigo, String nombre, int piso, double distancia,
                               int filas, int asientosPorFila, int capacidad) {
        if (tipo == null) {
            throw new IllegalArgumentException("Elegí el tipo de sector");
        }
        return switch (tipo) {
            case NUMERADO -> crearNumerado(codigo, nombre, piso, distancia, filas, asientosPorFila);
            case GENERAL -> new SectorGeneral(codigo, nombre, piso, distancia, capacidad);
        };
    }

    public static SectorNumerado crearNumerado(String codigo, String nombre, int piso, double distancia,
                                               int filas, int asientosPorFila) {
        SectorNumerado sector = new SectorNumerado(codigo, nombre, piso, distancia);
        sector.generarAsientos(filas, asientosPorFila);
        return sector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sector otro)) {
            return false;
        }
        return codigo.equals(otro.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + " · " + nombre;
    }
}
