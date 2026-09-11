package bicicletas;

public abstract class Cliente {

    protected String dni;
    protected String nombre;
    protected String apellido;

    public Cliente(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /** Cada subclase decide si aplica descuento sobre la tarifa base. */
    public abstract boolean esSuscripto();
}
