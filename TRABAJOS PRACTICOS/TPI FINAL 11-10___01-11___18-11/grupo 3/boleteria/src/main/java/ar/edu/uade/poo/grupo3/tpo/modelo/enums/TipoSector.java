package ar.edu.uade.poo.grupo3.tpo.modelo.enums;

public enum TipoSector {
    NUMERADO("Numerado"),
    GENERAL("General (de pie)");

    private final String descripcion;

    TipoSector(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
