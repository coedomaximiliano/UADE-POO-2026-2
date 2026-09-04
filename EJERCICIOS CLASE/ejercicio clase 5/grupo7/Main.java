import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        Medico medico = new Medico("Dra. Ana Perez", "Clinica medica");
        Paciente paciente = new Paciente("Emanuel Buczek", "1007");

        Turno turno = new Turno(
                LocalDate.of(2026, 9, 4),
                LocalTime.of(18, 30),
                medico,
                paciente
        );

        turno.mostrarDetalle();

        System.out.println();
        System.out.println("Cantidad de turnos del medico: " + medico.getTurnos().size());
        System.out.println("Cantidad de turnos del paciente: " + paciente.getTurnos().size());
    }
}
