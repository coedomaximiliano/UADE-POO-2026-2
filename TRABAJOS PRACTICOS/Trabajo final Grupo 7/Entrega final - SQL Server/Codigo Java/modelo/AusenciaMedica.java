package modelo;

import java.time.LocalDate;

public class AusenciaMedica {
    private int id;
    private String matricula, motivo;
    private LocalDate desde, hasta;
    private boolean activa;

    public AusenciaMedica(
            int id,
            String matricula,
            LocalDate desde,
            LocalDate hasta,
            String motivo,
            boolean activa) {
        if (matricula == null
                || matricula.isBlank()
                || desde == null
                || hasta == null
                || hasta.isBefore(desde)
                || motivo == null
                || motivo.isBlank()
                || motivo.trim().length() > 120)
            throw new IllegalArgumentException(
                    "Revisá el médico, las fechas y el motivo (hasta 120 caracteres).");
        this.id = id;
        this.matricula = matricula;
        this.desde = desde;
        this.hasta = hasta;
        this.motivo = motivo.trim();
        this.activa = activa;
    }

    public int getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean isActiva() {
        return activa;
    }

    public boolean incluye(LocalDate fecha) {
        return activa && !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }
}
