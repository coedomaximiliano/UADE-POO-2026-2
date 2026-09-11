/**
 * Clase abstracta que representa a un jugador de futbol. Extiende Persona.
 * Las posiciones concretas (Arquero, Defensor, Mediocampo, Delantero)
 * heredan de esta clase.
 */
public abstract class Jugador extends Persona {

    protected boolean esTitular;

    public Jugador(String nombre, String apellido, int edad, boolean esTitular) {
        super(nombre, apellido, edad);
        this.esTitular = esTitular;
    }

    public boolean isEsTitular() {
        return esTitular;
    }
}
