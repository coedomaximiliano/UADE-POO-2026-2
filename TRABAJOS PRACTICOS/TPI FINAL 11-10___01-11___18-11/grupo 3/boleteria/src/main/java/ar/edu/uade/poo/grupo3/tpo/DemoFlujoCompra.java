package ar.edu.uade.poo.grupo3.tpo;

import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.MedioPago;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.DevolucionNoPermitidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.ReservaVencidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Cliente;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Devolucion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Factura;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Recorre por consola el flujo de compra completo, incluidos los casos de error:
 * registro e inicio de sesión, asiento suelto, reserva de 5 minutos vencida, preventa,
 * promociones, factura y devoluciones.
 *
 * Ejecutar: mvn compile exec:java -Dexec.mainClass=ar.edu.uade.poo.grupo3.tpo.DemoFlujoCompra
 */
public class DemoFlujoCompra {

    public static void main(String[] args) throws Exception {
        Boleteria boleteria = Boleteria.getInstancia();
        DatosIniciales.inicializar(boleteria);
        LocalDateTime ahora = LocalDateTime.now();

        titulo("1. Registro de cuenta e inicio de sesión");
        Cliente diego = boleteria.registrarCuenta("40123456", "Diego Ruiz", "diego@mail.com", "clave123");
        System.out.println("Cuenta creada: " + diego + " · " + diego.getEmail());
        try {
            boleteria.registrarCuenta("40999888", "Otro Diego", "diego@mail.com", "clave123");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Registro rechazado: " + e.getMessage());
        }
        if (boleteria.iniciarSesion("diego@mail.com", "equivocada").isEmpty()) {
            System.out.println("✗ Login rechazado: email o contraseña incorrectos");
        }
        Cliente cliente = boleteria.iniciarSesion("diego@mail.com", "clave123").orElseThrow();
        System.out.println("✓ Sesión iniciada: " + cliente.getNombre());

        titulo("2. Búsqueda de funciones y disponibilidad");
        List<Funcion> funciones = boleteria.buscarFunciones("Satélites");
        funciones.forEach(f -> System.out.println("  #" + f.getId() + "  " + f
                + (f.estaEnPreventa(ahora) ? "  [PREVENTA]" : "")));
        Funcion funcion = funciones.stream().filter(f -> f.getId() == 1).findFirst().orElseThrow();
        SectorNumerado floor = (SectorNumerado) funcion.getSede().buscarSector("FLR").orElseThrow();
        System.out.println("Floor: " + boleteria.consultarDisponibilidad(funcion, floor, ahora).size()
                + " asientos libres · $" + (long) funcion.calcularPrecio(floor) + " cada uno");

        titulo("3. Regla de venta: no dejar un asiento suelto");
        Compra intento = boleteria.iniciarCompra(cliente, "", ahora);
        try {
            boleteria.reservarAsientos(intento, funcion, floor, asientos(floor, "A", 1, 3), ahora);
        } catch (SeleccionInvalidaException e) {
            System.out.println("✗ " + e.getMessage());
            boleteria.cancelarCompra(intento);
        }

        titulo("4. Compra confirmada dentro de los 5 minutos (código promocional + paquete VIP)");
        Compra compra = boleteria.iniciarCompra(cliente, "", ahora);
        boleteria.reservarAsientos(compra, funcion, floor, asientos(floor, "A", 1, 2, 3, 4), ahora);
        boleteria.agregarPaquete(compra, boleteria.getPaquetes().get(0));
        boleteria.aplicarCodigoPromocional(compra, "FAN10");
        try {
            boleteria.aplicarCodigoPromocional(compra, "GRATIS100");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        }
        System.out.println("Reserva #" + compra.getNroCompra() + " · vence en "
                + compra.getSegundosRestantes(ahora) / 60 + " minutos");
        Factura factura = boleteria.confirmarCompra(compra, MedioPago.TARJETA_CREDITO, ahora.plusMinutes(2));
        System.out.println(factura.generarTexto());
        System.out.println("Factura guardada en datos/facturas/FAC-" + factura.getNumero() + ".txt");

        titulo("5. Reserva vencida: pasan 6 minutos sin pagar");
        Compra lenta = boleteria.iniciarCompra(cliente, "", ahora);
        boleteria.reservarAsientos(lenta, funcion, floor, asientos(floor, "B", 1, 2), ahora);
        try {
            boleteria.confirmarCompra(lenta, MedioPago.EFECTIVO, ahora.plusMinutes(6));
        } catch (ReservaVencidaException e) {
            System.out.println("✗ " + e.getMessage());
        }
        System.out.println("B-1 vuelve a estar libre: "
                + funcion.estaDisponible(floor, floor.buscarAsiento("B", 1).orElseThrow()));

        titulo("6. Preventa con código de acceso + código promocional");
        Funcion conPreventa = funciones.stream().filter(f -> f.getId() == 2).findFirst().orElseThrow();
        SectorNumerado nivel100 = (SectorNumerado) conPreventa.getSede().buscarSector("100").orElseThrow();
        Compra sinCodigo = boleteria.iniciarCompra(cliente, "", ahora);
        try {
            boleteria.reservarAsientos(sinCodigo, conPreventa, nivel100, asientos(nivel100, "A", 1, 2), ahora);
        } catch (SeleccionInvalidaException e) {
            System.out.println("✗ " + e.getMessage());
            boleteria.cancelarCompra(sinCodigo);
        }
        Compra conCodigo = boleteria.iniciarCompra(cliente, "ORBITA26", ahora);
        boleteria.reservarAsientos(conCodigo, conPreventa, nivel100, asientos(nivel100, "A", 1, 2), ahora);
        boleteria.aplicarCodigoPromocional(conCodigo, "BANCO20");
        Factura facturaPreventa = boleteria.confirmarCompra(conCodigo, MedioPago.TARJETA_DEBITO, ahora);
        System.out.println("✓ Compra en preventa · " + conCodigo.getPromocion().getDescripcion()
                + " · total $" + (long) facturaPreventa.getTotal());

        titulo("7. Devoluciones");
        String codigo = compra.getEntradas().get(0).getCodigo();
        Devolucion devolucion = boleteria.devolverEntrada(codigo, "No puedo asistir", ahora);
        System.out.println("✓ Entrada " + codigo + " devuelta · reintegro $"
                + (long) devolucion.getMontoReintegro() + " (sin cargos) · A-1 libre otra vez: "
                + funcion.estaDisponible(floor, floor.buscarAsiento("A", 1).orElseThrow()));

        Funcion manana = funciones.stream().filter(f -> f.getId() == 3).findFirst().orElseThrow();
        SectorNumerado nivel400 = (SectorNumerado) manana.getSede().buscarSector("400").orElseThrow();
        Compra deUltimoMomento = boleteria.iniciarCompra(cliente, "", ahora);
        boleteria.reservarAsientos(deUltimoMomento, manana, nivel400, asientos(nivel400, "E", 1, 2), ahora);
        boleteria.confirmarCompra(deUltimoMomento, MedioPago.TRANSFERENCIA, ahora);
        try {
            boleteria.devolverEntrada(deUltimoMomento.getEntradas().get(0).getCodigo(), "Cambio de planes", ahora);
        } catch (DevolucionNoPermitidaException e) {
            System.out.println("✗ " + e.getMessage());
        }
        System.out.println("\nFin de la demo.");
    }

    private static List<Asiento> asientos(SectorNumerado sector, String fila, int... numeros) {
        return java.util.Arrays.stream(numeros)
                .mapToObj(n -> sector.buscarAsiento(fila, n).orElseThrow())
                .toList();
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }
}
