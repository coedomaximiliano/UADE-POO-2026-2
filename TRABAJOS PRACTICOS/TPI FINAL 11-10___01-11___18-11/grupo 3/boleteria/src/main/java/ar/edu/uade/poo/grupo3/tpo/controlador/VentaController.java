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
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Devolucion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
        if (compra == null) {
            return false;
        }
        for (Entrada e : compra.getEntradasActivas()) {
            if (e.getSector().equals(sector) && asiento.equals(e.getAsiento()) && e.getFuncion() == funcion) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inicia una compra y reserva los lugares: los asientos elegidos si el sector es numerado,
     * o la cantidad pedida si es general. Si la selección no es válida, la compra se cancela
     * y la excepción llega a la pantalla para mostrar el mensaje.
     */
    public void reservar(Cliente cliente, Funcion funcion, Sector sector, List<Asiento> seleccion, int cantidad)
            throws SeleccionInvalidaException {
        Compra nueva = boleteria.iniciarCompra(cliente, ahora());
        try {
            if (sector instanceof SectorNumerado numerado) {
                boleteria.reservarAsientos(nueva, funcion, numerado, seleccion, ahora());
            } else {
                boleteria.reservarAsientos(nueva, funcion, (SectorGeneral) sector, cantidad, ahora());
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

    /** Devuelve null si no existe. */
    public Entrada buscarEntrada(String codigo) {
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
