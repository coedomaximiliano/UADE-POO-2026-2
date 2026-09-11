package consultorio;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Medico medico = new Medico("M-001", "Laura", "Fernandez", "Cardiologia");
        Paciente paciente = new Paciente("HC-045", "Juan", "Perez");

        Turno turno1 = new Turno(LocalDate.of(2026, 9, 10), "10:30", medico, paciente);
        Turno turno2 = new Turno(LocalDate.of(2026, 9, 17), "11:00", medico, paciente);

        turno1.atender();
        turno2.cancelar();

        System.out.println("Medico: " + medico.getNombreCompleto() + " (" + medico.getEspecialidad() + ")");
        System.out.println("Paciente: " + paciente.getNombreCompleto() + " - HC " + paciente.getNroHistoriaClinica());
        System.out.println("Turno1: " + turno1.getFecha() + " - estado " + turno1.getEstado());
        System.out.println("Turno2: " + turno2.getFecha() + " - estado " + turno2.getEstado());
    }
}
