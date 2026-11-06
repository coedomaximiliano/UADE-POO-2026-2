package ar.edu.uade.poo.grupo3.tpo.modelo.ventas;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoEntrada;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoFuncion;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;

import java.time.LocalDateTime;

public class Entrada {

    private final String codigo;
    private final Funcion funcion;
    private final Sector sector;
    private final Asiento asiento;
    private final double precio;
    private EstadoEntrada estado;
    private double valorPagado;

    /** El asiento es null cuando el sector es general. */
    public Entrada(String codigo, Funcion funcion, Sector sector, Asiento asiento) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la entrada es obligatorio");
        }
        if (funcion == null || sector == null) {
            throw new IllegalArgumentException("La función y el sector son obligatorios");
        }
        this.codigo = codigo;
        this.funcion = funcion;
        this.sector = sector;
        this.asiento = asiento;
        this.precio = calcularPrecio();
        this.estado = EstadoEntrada.RESERVADA;
    }

    /** Precio = precio base de la función × multiplicador del sector (piso y cercanía). */
    public double calcularPrecio() {
        return funcion.calcularPrecio(sector);
    }

    /** Se paga por lo que vale menos la parte de la promoción que le toca; es lo que se devuelve. */
    public void pagar(double valorPagado) {
        estado = EstadoEntrada.PAGADA;
        this.valorPagado = valorPagado;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    /** La entrada deja de ocupar lugar (la reserva venció o se canceló). */
    public void anular() {
        estado = EstadoEntrada.ANULADA;
        funcion.actualizarEstado();
    }

    /** Se puede devolver si está pagada y faltan 48 hs o más, o si la función se canceló. */
    public boolean puedeDevolverse(LocalDateTime ahora) {
        if (estado != EstadoEntrada.PAGADA) {
            return false;
        }
        if (funcion.getEstado() == EstadoFuncion.CANCELADA) {
            return true;
        }
        return !ahora.plusHours(Devolucion.HORAS_LIMITE).isAfter(funcion.getFechaHora());
    }

    public void devolver() {
        estado = EstadoEntrada.DEVUELTA;
        funcion.actualizarEstado();
    }

    public String getUbicacion() {
        return sector.getNombre() + (asiento == null ? " (general)" : " · Fila " + asiento.getFila()
                + " · Asiento " + asiento.getNumero());
    }

    public String getCodigo() {
        return codigo;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Sector getSector() {
        return sector;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public double getPrecio() {
        return precio;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return codigo + " · " + getUbicacion() + " · " + estado;
    }
}
