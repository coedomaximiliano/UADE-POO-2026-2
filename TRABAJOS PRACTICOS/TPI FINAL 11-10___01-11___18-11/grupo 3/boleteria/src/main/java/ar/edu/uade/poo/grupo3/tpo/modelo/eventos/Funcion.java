package ar.edu.uade.poo.grupo3.tpo.modelo.eventos;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoFuncion;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fecha concreta de un evento en una sede. Es la experta en disponibilidad (GRASP):
 * el mismo asiento físico se vende por separado en cada función.
 */
public class Funcion {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int id;
    private final Evento evento;
    private final LocalDateTime fechaHora;
    private final double precioBase;
    private final Sede sede;
    private EstadoFuncion estado;
    private final List<Entrada> entradas;

    public Funcion(int id, Evento evento, LocalDateTime fechaHora, double precioBase, Sede sede) {
        if (evento == null || sede == null) {
            throw new IllegalArgumentException("El evento y la sede son obligatorios");
        }
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora son obligatorias");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor a cero");
        }
        this.id = id;
        this.evento = evento;
        this.fechaHora = fechaHora;
        this.precioBase = precioBase;
        this.sede = sede;
        this.estado = EstadoFuncion.PROGRAMADA;
        this.entradas = new ArrayList<>();
    }

    /** Un asiento (o un lugar en un sector general) está libre si ninguna entrada activa lo ocupa. */
    public boolean estaDisponible(Sector sector, Asiento asiento) {
        if (estado != EstadoFuncion.PROGRAMADA || !perteneceASede(sector)) {
            return false;
        }
        if (sector instanceof SectorNumerado numerado) {
            if (asiento == null || !numerado.getAsientos().contains(asiento)) {
                return false;
            }
            for (Entrada e : entradas) {
                if (e.getEstado().estaActiva() && e.getSector().equals(sector) && asiento.equals(e.getAsiento())) {
                    return false;
                }
            }
            return true;
        }
        return lugaresLibres(sector) > 0;
    }

    public List<Asiento> getAsientosLibres(SectorNumerado sector) {
        List<Asiento> libres = new ArrayList<>();
        for (Asiento a : sector.getAsientos()) {
            if (estaDisponible(sector, a)) {
                libres.add(a);
            }
        }
        return libres;
    }

    public int lugaresOcupados(Sector sector) {
        int ocupados = 0;
        for (Entrada e : entradas) {
            if (e.getEstado().estaActiva() && e.getSector().equals(sector)) {
                ocupados++;
            }
        }
        return ocupados;
    }

    public int lugaresLibres(Sector sector) {
        return Math.max(0, sector.getCapacidad() - lugaresOcupados(sector));
    }

    /** Porcentaje de ocupación del sector (0 a 100). */
    public double getOcupacion(Sector sector) {
        if (sector.getCapacidad() == 0) {
            return 0;
        }
        return lugaresOcupados(sector) * 100.0 / sector.getCapacidad();
    }

    public void registrarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada no puede ser nula");
        }
        entradas.add(entrada);
        actualizarEstado();
    }

    /** Pasa a AGOTADA cuando no queda lugar y vuelve a PROGRAMADA si se libera alguno. */
    public void actualizarEstado() {
        if (estado == EstadoFuncion.CANCELADA || estado == EstadoFuncion.FINALIZADA) {
            return;
        }
        boolean agotada = true;
        for (Sector s : sede.getSectores()) {
            if (lugaresLibres(s) != 0) {
                agotada = false;
            }
        }
        estado = agotada ? EstadoFuncion.AGOTADA : EstadoFuncion.PROGRAMADA;
    }

    public void cancelar() {
        estado = EstadoFuncion.CANCELADA;
    }

    private boolean perteneceASede(Sector sector) {
        return sector != null && sede.getSectores().contains(sector);
    }

    public double calcularPrecio(Sector sector) {
        return Math.round(precioBase * sector.calcularMultiplicador());
    }

    public int getId() {
        return id;
    }

    public Evento getEvento() {
        return evento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public Sede getSede() {
        return sede;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public void setEstado(EstadoFuncion estado) {
        if (estado != null) {
            this.estado = estado;
        }
    }

    public List<Entrada> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    @Override
    public String toString() {
        return evento.getNombre() + " · " + sede.getNombre() + " · " + fechaHora.format(FORMATO);
    }
}
