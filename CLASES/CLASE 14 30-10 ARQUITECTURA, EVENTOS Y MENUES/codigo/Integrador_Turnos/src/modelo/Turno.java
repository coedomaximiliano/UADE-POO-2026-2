package modelo;

public class Turno {
    private String fecha;      // dd/mm
    private String hora;       // hh:mm
    private String paciente;
    private String medico;

    public Turno(String fecha, String hora, String paciente, String medico) {
        this.fecha = fecha;
        this.hora = hora;
        this.paciente = paciente;
        this.medico = medico;
    }

    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getPaciente() { return paciente; }
    public String getMedico() { return medico; }

    // Dos turnos chocan si son del mismo medico, dia y hora
    public boolean seSuperponeCon(Turno otro) {
        return medico.equalsIgnoreCase(otro.medico)
            && fecha.equals(otro.fecha) && hora.equals(otro.hora);
    }

    public String aLinea() {
        return fecha + ";" + hora + ";" + paciente + ";" + medico;
    }

    public static Turno desdeLinea(String linea) {
        String[] p = linea.split(";");
        return new Turno(p[0], p[1], p[2], p[3]);
    }
}
