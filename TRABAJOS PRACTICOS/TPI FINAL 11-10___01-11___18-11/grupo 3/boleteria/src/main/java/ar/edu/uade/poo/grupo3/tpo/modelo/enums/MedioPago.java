package ar.edu.uade.poo.grupo3.tpo.modelo.enums;

public enum MedioPago {
    TARJETA_CREDITO("Tarjeta de crédito"),
    TARJETA_DEBITO("Tarjeta de débito"),
    TRANSFERENCIA("Transferencia"),
    EFECTIVO("Efectivo");

    private final String descripcion;

    MedioPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
