package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class HorarioAtencion {
    private int dia;
    private LocalTime desde;
    private LocalTime hasta;

    public HorarioAtencion(int dia, LocalTime desde, LocalTime hasta) {
        if (dia < 1
                || dia > 7
                || desde == null
                || hasta == null
                || !desde.isBefore(hasta)
                || desde.getMinute() % 30 != 0
                || hasta.getMinute() % 30 != 0
                || desde.getSecond() != 0
                || hasta.getSecond() != 0
                || desde.getNano() != 0
                || hasta.getNano() != 0) {
            throw new IllegalArgumentException(
                    "Revisá los días y horarios de atención. Usar pasos de 30 minutos, desde menor que hasta.");
        }
        this.dia = dia;
        this.desde = desde;
        this.hasta = hasta;
    }

    public int getDia() {
        return dia;
    }

    public LocalTime getDesde() {
        return desde;
    }

    public LocalTime getHasta() {
        return hasta;
    }

    public boolean incluye(LocalDate fecha, LocalTime hora) {
        return fecha != null
                && hora != null
                && fecha.getDayOfWeek().getValue() == dia
                && !hora.isBefore(desde)
                && !hora.isAfter(hasta.minusMinutes(30));
    }

    public String toString() {
        return new String[] {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"}[dia - 1]
                + " "
                + desde
                + "–"
                + hasta;
    }
}
