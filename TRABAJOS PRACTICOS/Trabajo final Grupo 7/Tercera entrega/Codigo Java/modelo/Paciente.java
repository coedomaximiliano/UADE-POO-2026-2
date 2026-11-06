package modelo;

public class Paciente extends Persona {
    private String numeroHistoriaClinica;

    public Paciente(String nombre, String dni, String email, String numeroHistoriaClinica) {
        super(nombre, dni, email);
        if (numeroHistoriaClinica == null || numeroHistoriaClinica.trim().isEmpty()) {
            throw new IllegalArgumentException("Falta el numero de historia clinica.");
        }
        this.numeroHistoriaClinica = numeroHistoriaClinica.trim();
    }

    public String getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public void actualizarHistoriaClinica(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("Falta el numero de historia clinica.");
        }
        numeroHistoriaClinica = numero.trim();
    }

    @Override
    public String mostrarDatos() {
        return getNombre() + " - DNI: " + getDni()
                + " - Historia clinica: " + numeroHistoriaClinica;
    }
}
