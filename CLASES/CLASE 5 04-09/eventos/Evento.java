package eventos;

import java.time.LocalDate;

/**
 * Evento concentra tres ASOCIACIONES (con Cliente, Organizador y Salon,
 * todas 1 a 0..* desde el lado "uno") y una DEPENDENCIA con Calendario
 * (usada solo dentro de fechaLimiteSenia(), nunca como atributo).
 */
public class Evento {

    private String nombre;
    private LocalDate fecha;
    private double montoTotal;
    private double montoSenia;
    private Cliente cliente;
    private Organizador organizador;
    private Salon salon;

    public Evento(String nombre, LocalDate fecha, double montoTotal, double montoSenia,
                  Cliente cliente, Organizador organizador, Salon salon) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
        this.montoSenia = montoSenia;
        this.cliente = cliente;
        this.organizador = organizador;
        this.salon = salon;
    }

    public LocalDate fechaLimiteSenia() {
        Calendario calendario = new Calendario(); // dependencia: variable local, no atributo
        return calendario.restarDias(fecha, 15);
    }

    public String getNombre() {
        return nombre;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Organizador getOrganizador() {
        return organizador;
    }

    public Salon getSalon() {
        return salon;
    }
}
