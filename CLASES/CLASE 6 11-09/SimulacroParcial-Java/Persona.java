/**
 * Clase abstracta que representa a una persona vinculada a un equipo de
 * futbol (tecnico o jugador). Agrupa los datos comunes a ambos.
 */
public abstract class Persona {

    protected String nombre;
    protected String apellido;
    protected int edad;

    public Persona(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getEdad() {
        return edad;
    }
}
