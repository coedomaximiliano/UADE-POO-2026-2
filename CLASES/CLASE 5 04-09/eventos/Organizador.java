package eventos;

public abstract class Organizador {

    protected String nombre;
    protected String telefono;

    public Organizador(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }
}
