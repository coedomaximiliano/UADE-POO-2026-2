package consultorio;

public class Paciente {

    private String nroHistoriaClinica;
    private String nombre;
    private String apellido;

    public Paciente(String nroHistoriaClinica, String nombre, String apellido) {
        this.nroHistoriaClinica = nroHistoriaClinica;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getNroHistoriaClinica() {
        return nroHistoriaClinica;
    }
}
