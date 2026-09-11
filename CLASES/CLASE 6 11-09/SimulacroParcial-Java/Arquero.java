/**
 * Jugador que ocupa la posicion de arquero.
 */
public class Arquero extends Jugador {

    private int golesRecibidos;

    public Arquero(String nombre, String apellido, int edad, boolean esTitular, int golesRecibidos) {
        super(nombre, apellido, edad, esTitular);
        this.golesRecibidos = golesRecibidos;
    }

    @Override
    public String toString() {
        return String.format("Arquero: %s %s (%d anios) - %d goles recibidos - %s",
                nombre, apellido, edad, golesRecibidos, esTitular ? "titular" : "suplente");
    }
}
