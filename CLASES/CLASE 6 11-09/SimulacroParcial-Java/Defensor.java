/**
 * Jugador que ocupa la posicion de defensor. No tiene atributos propios
 * ademas de los heredados de Jugador/Persona.
 */
public class Defensor extends Jugador {

    public Defensor(String nombre, String apellido, int edad, boolean esTitular) {
        super(nombre, apellido, edad, esTitular);
    }

    @Override
    public String toString() {
        return String.format("Defensor: %s %s (%d anios) - %s",
                nombre, apellido, edad, esTitular ? "titular" : "suplente");
    }
}
