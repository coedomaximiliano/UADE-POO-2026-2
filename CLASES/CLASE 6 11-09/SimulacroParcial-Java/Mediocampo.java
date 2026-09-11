/**
 * Jugador que ocupa la posicion de mediocampista.
 */
public class Mediocampo extends Jugador {

    private int numeroAsistencias;

    public Mediocampo(String nombre, String apellido, int edad, boolean esTitular, int numeroAsistencias) {
        super(nombre, apellido, edad, esTitular);
        this.numeroAsistencias = numeroAsistencias;
    }

    @Override
    public String toString() {
        return String.format("Mediocampo: %s %s (%d anios) - %d asistencias - %s",
                nombre, apellido, edad, numeroAsistencias, esTitular ? "titular" : "suplente");
    }
}
