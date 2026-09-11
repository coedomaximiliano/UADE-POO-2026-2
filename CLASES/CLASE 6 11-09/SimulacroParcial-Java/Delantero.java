/**
 * Jugador que ocupa la posicion de delantero.
 */
public class Delantero extends Jugador {

    private int golesAnotados;

    public Delantero(String nombre, String apellido, int edad, boolean esTitular, int golesAnotados) {
        super(nombre, apellido, edad, esTitular);
        this.golesAnotados = golesAnotados;
    }

    @Override
    public String toString() {
        return String.format("Delantero: %s %s (%d anios) - %d goles anotados - %s",
                nombre, apellido, edad, golesAnotados, esTitular ? "titular" : "suplente");
    }
}
