package ar.edu.uade.poo.grupo3.tpo.controlador;

import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.MedioPago;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.DevolucionNoPermitidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.ReservaVencidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorGeneral;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.promociones.Promocion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Cliente;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Factura;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.PaqueteVIP;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Devolucion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de la venta de entradas (patrón MVC): la pantalla le avisa qué hizo el usuario,
 * él lleva la compra en curso y el reloj de la reserva, y le pide las operaciones a la Boleteria (modelo).
 */
public class VentaController {

    private final Boleteria boleteria;
    /** Compra reservada que todavía no se pagó ni se canceló; null si no hay ninguna. */
    private Compra compra;
    /** Minutos "adelantados" por la demo para mostrar cómo vence una reserva. */
    private Duration desfaseReloj = Duration.ZERO;

    public VentaController(Boleteria boleteria) {
        this.boleteria = boleteria;
    }

    // ------------------------------------------------------------------ datos para armar la pantalla

    public List<Funcion> funcionesDisponibles() {
        return boleteria.buscarFunciones("");
    }

    public List<Cliente> clientes() {
        return boleteria.getClientes();
    }

    public Optional<Cliente> clienteSesion() {
        return boleteria.getClienteSesion();
    }

    /** El paquete VIP que se ofrece en la venta, o null si no hay ninguno cargado. */
    public PaqueteVIP paqueteVip() {
        return boleteria.getPaquetes().isEmpty() ? null : boleteria.getPaquetes().get(0);
    }

    public boolean estaEnPreventa(Funcion funcion) {
        return funcion.estaEnPreventa(ahora());
    }

    public void liberarReservasVencidas() {
        boleteria.liberarReservasVencidas(ahora());
    }

    // ------------------------------------------------------------------ compra en curso

    public boolean hayCompraEnCurso() {
        return compra != null;
    }

    public Compra getCompra() {
        return compra;
    }

    /** Si el asiento está reservado por la compra en curso (para pintarlo como "tu reserva"). */
    public boolean estaReservado(Funcion funcion, Sector sector, Asiento asiento) {
        return compra != null && compra.getEntradasActivas().stream()
                .anyMatch(e -> e.getSector().equals(sector) && asiento.equals(e.getAsiento())
                        && e.getFuncion() == funcion);
    }

    /**
     * Inicia una compra y reserva los lugares: los asientos elegidos si el sector es numerado,
     * o la cantidad pedida si es general. Si alguna regla de venta falla, la compra se cancela
     * y la excepción llega a la pantalla para mostrar el mensaje.
     */
    public void reservar(Cliente cliente, String codigoPreventa, Funcion funcion, Sector sector,
                         List<Asiento> seleccion, int cantidad, boolean conPaqueteVip)
            throws SeleccionInvalidaException {
        Compra nueva = boleteria.iniciarCompra(cliente, codigoPreventa, ahora());
        try {
            if (sector instanceof SectorNumerado numerado) {
                boleteria.reservarAsientos(nueva, funcion, numerado, seleccion, ahora());
            } else {
                boleteria.reservarAsientos(nueva, funcion, (SectorGeneral) sector, cantidad, ahora());
            }
            if (conPaqueteVip && paqueteVip() != null) {
                boleteria.agregarPaquete(nueva, paqueteVip());
            }
            compra = nueva;
        } catch (SeleccionInvalidaException | IllegalStateException ex) {
            boleteria.cancelarCompra(nueva);
            throw ex;
        }
    }

    /** Aplica un código promocional a la compra en curso. IllegalArgumentException si el código no existe. */
    public Promocion aplicarPromocion(String codigo) {
        boleteria.aplicarCodigoPromocional(compra, codigo);
        return compra.getPromocion();
    }

    /** Confirma el pago y emite la factura. Haya salido bien o vencido la reserva, la compra deja de estar en curso. */
    public Factura pagar(MedioPago medio) throws ReservaVencidaException {
        try {
            return boleteria.confirmarCompra(compra, medio, ahora());
        } finally {
            compra = null;
        }
    }

    public void cancelar() {
        if (compra != null) {
            boleteria.cancelarCompra(compra);
            compra = null;
        }
    }

    // ------------------------------------------------------------------ cuenta regresiva

    /** Si la reserva en curso pasó los 5 minutos, la vence, libera los asientos y devuelve true. */
    public boolean verificarVencimiento() {
        if (compra == null || !compra.estaVencida(ahora())) {
            return false;
        }
        boleteria.liberarReservasVencidas(ahora());
        compra = null;
        return true;
    }

    public long segundosRestantes() {
        return compra == null ? 0 : compra.getSegundosRestantes(ahora());
    }

    /** Solo para la demo: simula que pasaron minutos para mostrar cómo vence la reserva. */
    public void adelantarReloj(int minutos) {
        desfaseReloj = desfaseReloj.plusMinutes(minutos);
    }

    public LocalDateTime ahora() {
        return LocalDateTime.now().plus(desfaseReloj);
    }


    // ------------------------------------------------------------------ devoluciones

    public Optional<Entrada> buscarEntrada(String codigo) {
        return boleteria.buscarEntrada(codigo);
    }

    public boolean puedeDevolverse(Entrada entrada) {
        return entrada.puedeDevolverse(ahora());
    }

    /** La devolución es independiente de la venta en curso: no toca la compra que pueda estar abierta. */
    public Devolucion devolverEntrada(String codigoEntrada, String motivo) throws DevolucionNoPermitidaException {
        return boleteria.devolverEntrada(codigoEntrada, motivo, ahora());
    }
}
