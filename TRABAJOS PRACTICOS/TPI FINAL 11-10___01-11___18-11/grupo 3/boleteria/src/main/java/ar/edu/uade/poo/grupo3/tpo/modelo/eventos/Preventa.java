package ar.edu.uade.poo.grupo3.tpo.modelo.eventos;

import java.time.LocalDateTime;

/** Período en el que solo se puede comprar con un código de acceso. No cambia el precio. */
public class Preventa {

    private final String codigoAcceso;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;

    public Preventa(String codigoAcceso, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (codigoAcceso == null || codigoAcceso.isBlank()) {
            throw new IllegalArgumentException("El código de preventa es obligatorio");
        }
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("Las fechas de la preventa no son válidas");
        }
        this.codigoAcceso = codigoAcceso.trim();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public boolean estaVigente(LocalDateTime ahora) {
        return ahora != null && !ahora.isBefore(fechaInicio) && !ahora.isAfter(fechaFin);
    }

    public boolean validarCodigo(String codigo) {
        return codigo != null && codigoAcceso.equalsIgnoreCase(codigo.trim());
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
}
