package ar.edu.uade.poo.grupo3.tpo.modelo.ventas;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoCompra;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.MedioPago;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.ReservaVencidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.promociones.Promocion;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Una compra reserva las ubicaciones por {@value #MINUTOS_RESERVA} minutos.
 * Si se paga a tiempo pasa a CONFIRMADA; si no, VENCIDA y los asientos se liberan.
 */
public class Compra {

    public static final int MINUTOS_RESERVA = 5;
    /** Cargo por servicio, en porcentaje sobre el subtotal con descuento. */
    public static final double CARGO_SERVICIO = 12;
    private static int secuencia = 1;

    private final int nroCompra;
    private final Cliente cliente;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime vencimientoReserva;
    private final String codigoPreventa;
    private EstadoCompra estado;
    private final List<Entrada> entradas;
    private final List<PaqueteVIP> paquetes;
    private Promocion promocion;
    private MedioPago medioPago;
    private String nroOperacion;

    public Compra(Cliente cliente, String codigoPreventa, LocalDateTime ahora) {
        if (cliente == null) {
            throw new IllegalArgumentException("La compra necesita un cliente");
        }
        this.nroCompra = secuencia++;
        this.cliente = cliente;
        this.fechaCreacion = ahora;
        this.vencimientoReserva = ahora.plusMinutes(MINUTOS_RESERVA);
        this.codigoPreventa = codigoPreventa == null ? "" : codigoPreventa.trim();
        this.estado = EstadoCompra.RESERVADA;
        this.entradas = new ArrayList<>();
        this.paquetes = new ArrayList<>();
    }

    /** Patrón Creador (GRASP): la compra crea sus entradas y las registra en la función. */
    public Entrada agregarEntrada(Funcion funcion, Sector sector, Asiento asiento) {
        verificarReservada();
        String codigo = String.format("E%04d-%02d", nroCompra, entradas.size() + 1);
        Entrada entrada = new Entrada(codigo, funcion, sector, asiento);
        entradas.add(entrada);
        funcion.registrarEntrada(entrada);
        return entrada;
    }

    public void agregarPaquete(PaqueteVIP paquete) {
        verificarReservada();
        paquete.reservarCupo();
        paquetes.add(paquete);
    }

    public void aplicarPromocion(Promocion promocion) {
        verificarReservada();
        if (promocion != null && !promocion.aplicaA(this)) {
            throw new IllegalArgumentException("La promoción no aplica a esta compra");
        }
        this.promocion = promocion;
    }

    public List<Entrada> getEntradasActivas() {
        return entradas.stream().filter(e -> e.getEstado().estaActiva()).toList();
    }

    public double calcularSubtotalEntradas() {
        return getEntradasActivas().stream().mapToDouble(Entrada::getPrecio).sum();
    }

    public double calcularSubtotalPaquetes() {
        return paquetes.stream().mapToDouble(PaqueteVIP::getPrecio).sum();
    }

    public double calcularSubtotal() {
        return calcularSubtotalEntradas() + calcularSubtotalPaquetes();
    }

    public double calcularDescuento() {
        if (promocion == null || !promocion.aplicaA(this)) {
            return 0;
        }
        return promocion.calcularDescuento(this);
    }

    /** El cargo por servicio se calcula sobre el subtotal ya descontado. */
    public double calcularCargos() {
        double base = calcularSubtotal() - calcularDescuento();
        return Math.round(base * CARGO_SERVICIO / 100.0);
    }

    public double calcularTotal() {
        return calcularSubtotal() - calcularDescuento() + calcularCargos();
    }

    public boolean estaVencida(LocalDateTime ahora) {
        return estado == EstadoCompra.RESERVADA && ahora.isAfter(vencimientoReserva);
    }

    public long getSegundosRestantes(LocalDateTime ahora) {
        return Math.max(0, Duration.between(ahora, vencimientoReserva).getSeconds());
    }

    /** Confirma el pago. El número de operación lo genera la boletería (no hay pasarela real). */
    public void confirmar(MedioPago medio, String nroOperacion, LocalDateTime ahora) throws ReservaVencidaException {
        if (medio == null) {
            throw new IllegalArgumentException("Elegí un medio de pago");
        }
        verificarReservada();
        if (estaVencida(ahora)) {
            vencer();
            throw new ReservaVencidaException("La reserva de la compra #" + nroCompra
                    + " venció: pasaron más de " + MINUTOS_RESERVA + " minutos. Elegí los asientos de nuevo.");
        }
        if (getEntradasActivas().isEmpty()) {
            throw new IllegalStateException("La compra no tiene entradas");
        }
        this.medioPago = medio;
        this.nroOperacion = nroOperacion;
        // El descuento de la promoción se reparte entre las entradas en proporción a su precio.
        double proporcionDescuento = calcularDescuento() / calcularSubtotalEntradas();
        for (Entrada entrada : getEntradasActivas()) {
            entrada.pagar(entrada.getPrecio() * (1 - proporcionDescuento));
        }
        estado = EstadoCompra.CONFIRMADA;
        cliente.agregarCompra(this);
    }

    public void vencer() {
        liberar(EstadoCompra.VENCIDA);
    }

    public void cancelar() {
        liberar(EstadoCompra.CANCELADA);
    }

    private void liberar(EstadoCompra nuevoEstado) {
        if (estado != EstadoCompra.RESERVADA) {
            return;
        }
        entradas.forEach(Entrada::anular);
        paquetes.forEach(PaqueteVIP::liberarCupo);
        estado = nuevoEstado;
    }

    private void verificarReservada() {
        if (estado != EstadoCompra.RESERVADA) {
            throw new IllegalStateException("La compra #" + nroCompra + " ya está " + estado);
        }
    }

    public int getNroCompra() {
        return nroCompra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getVencimientoReserva() {
        return vencimientoReserva;
    }

    public String getCodigoPreventa() {
        return codigoPreventa;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public List<Entrada> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    public List<PaqueteVIP> getPaquetes() {
        return Collections.unmodifiableList(paquetes);
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public String getNroOperacion() {
        return nroOperacion;
    }
}
