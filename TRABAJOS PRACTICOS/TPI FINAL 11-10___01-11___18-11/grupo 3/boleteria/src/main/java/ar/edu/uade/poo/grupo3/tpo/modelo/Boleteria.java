package ar.edu.uade.poo.grupo3.tpo.modelo;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoCompra;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoFuncion;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.MedioPago;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Evento;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.DevolucionNoPermitidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.ReservaVencidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorGeneral;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;
import ar.edu.uade.poo.grupo3.tpo.modelo.persistencia.ClienteRepositorioArchivo;
import ar.edu.uade.poo.grupo3.tpo.modelo.persistencia.EventoRepositorioArchivo;
import ar.edu.uade.poo.grupo3.tpo.modelo.persistencia.Repositorio;
import ar.edu.uade.poo.grupo3.tpo.modelo.persistencia.SedeRepositorioArchivo;
import ar.edu.uade.poo.grupo3.tpo.modelo.promociones.Promocion;
import ar.edu.uade.poo.grupo3.tpo.modelo.reglas.ReglaDeVenta;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Cliente;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Devolucion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Factura;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.PaqueteVIP;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Fachada del modelo: patrón Singleton + Controlador (GRASP), única puerta de entrada a las operaciones del sistema.
 * En términos de MVC es parte del modelo; los controladores de MVC (SedeController, VentaController) y la demo
 * por consola le hablan a la Boleteria, nunca directo al resto del dominio.
 */
public final class Boleteria {

    private static Boleteria instancia;

    private final Path directorioDatos;
    private final Repositorio<Sede> repoSedes;
    private final Repositorio<Cliente> repoClientes;
    private final EventoRepositorioArchivo repoEventos;

    private final List<Sede> sedes = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<Factura> facturas = new ArrayList<>();
    private final List<Devolucion> devoluciones = new ArrayList<>();
    private final List<ReglaDeVenta> reglas = new ArrayList<>();
    private final List<Promocion> promociones = new ArrayList<>();
    private final List<PaqueteVIP> paquetes = new ArrayList<>();

    /** Cliente autenticado en la app Swing; null si no hay sesión. */
    private Cliente clienteSesion;

    private static final Pattern PATRON_NUMERO_FACTURA =
            Pattern.compile("FAC-0001-(\\d{8})\\.txt", Pattern.CASE_INSENSITIVE);

    private Boleteria(Path directorioDatos) {
        this.directorioDatos = directorioDatos;
        this.repoSedes = new SedeRepositorioArchivo(directorioDatos);
        this.repoClientes = new ClienteRepositorioArchivo(directorioDatos);
        this.repoEventos = new EventoRepositorioArchivo(directorioDatos);
    }

    public static synchronized Boleteria getInstancia() {
        if (instancia == null) {
            instancia = new Boleteria(Path.of("datos"));
        }
        return instancia;
    }

    // ------------------------------------------------------------------ persistencia

    public void cargarDatos() throws IOException {
        sedes.clear();
        sedes.addAll(repoSedes.cargarTodos());
        clientes.clear();
        clientes.addAll(repoClientes.cargarTodos());
        repoEventos.prepararCarga(sedes);
        eventos.clear();
        eventos.addAll(repoEventos.cargarTodos());
        cargarSecuenciaFactura();
    }

    public void guardarSedes() throws IOException {
        repoSedes.guardarTodos(sedes);
    }

    public void guardarClientes() throws IOException {
        repoClientes.guardarTodos(clientes);
    }

    public void guardarEventos() throws IOException {
        repoEventos.guardarTodos(eventos);
    }

    public Path getDirectorioDatos() {
        return directorioDatos;
    }

    // ------------------------------------------------------------------ sedes (ABM)

    public List<Sede> getSedes() {
        return Collections.unmodifiableList(sedes);
    }

    public Optional<Sede> buscarSede(String nombre) {
        return sedes.stream().filter(s -> s.getNombre().equalsIgnoreCase(nombre.trim())).findFirst();
    }

    public void agregarSede(Sede sede) {
        if (sede == null) {
            throw new IllegalArgumentException("La sede no puede ser nula");
        }
        if (buscarSede(sede.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una sede llamada " + sede.getNombre());
        }
        if (sede.getSectores().isEmpty()) {
            throw new IllegalArgumentException("La sede necesita al menos un sector");
        }
        sedes.add(sede);
    }

    public void modificarSede(Sede sede, String nuevoNombre, String nuevaDireccion) {
        Optional<Sede> otra = buscarSede(nuevoNombre);
        if (otra.isPresent() && otra.get() != sede) {
            throw new IllegalArgumentException("Ya existe otra sede llamada " + nuevoNombre);
        }
        sede.setNombre(nuevoNombre);
        sede.setDireccion(nuevaDireccion);
    }

    public void agregarSectorASede(Sede sede, Sector sector) {
        verificarSinFunciones(sede, "agregarle sectores");
        sede.agregarSector(sector);
    }

    public void quitarSectorDeSede(Sede sede, String codigo) {
        verificarSinFunciones(sede, "quitarle sectores");
        if (sede.getSectores().size() == 1) {
            throw new IllegalStateException("La sede tiene que quedar con al menos un sector");
        }
        sede.quitarSector(codigo);
    }

    public void eliminarSede(Sede sede) {
        verificarSinFunciones(sede, "darla de baja");
        sedes.remove(sede);
    }

    public List<Funcion> getFuncionesActivas(Sede sede) {
        return eventos.stream().flatMap(e -> e.getFunciones().stream())
                .filter(f -> f.getSede() == sede)
                .filter(f -> f.getEstado() == EstadoFuncion.PROGRAMADA || f.getEstado() == EstadoFuncion.AGOTADA)
                .toList();
    }

    private void verificarSinFunciones(Sede sede, String accion) {
        int cantidad = getFuncionesActivas(sede).size();
        if (cantidad > 0) {
            throw new IllegalStateException("No se puede " + accion + ": " + sede.getNombre() + " tiene "
                    + cantidad + " función(es) programada(s).");
        }
    }

    // ------------------------------------------------------------------ cuentas

    public Cliente registrarCuenta(String dni, String nombre, String email, String contrasenia) {
        Cliente cliente = new Cliente(dni, nombre, email, contrasenia);
        if (buscarClientePorEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya hay una cuenta con el email " + cliente.getEmail());
        }
        if (clientes.stream().anyMatch(c -> c.getDni().equals(cliente.getDni()))) {
            throw new IllegalArgumentException("Ya hay una cuenta con el DNI " + cliente.getDni());
        }
        clientes.add(cliente);
        return cliente;
    }

    /** Devuelve el cliente si el email y la contraseña coinciden; vacío si no. Deja la sesión activa en la UI. */
    public Optional<Cliente> iniciarSesion(String email, String contrasenia) {
        Optional<Cliente> cliente = buscarClientePorEmail(email)
                .filter(c -> c.verificarContrasenia(contrasenia));
        cliente.ifPresent(c -> clienteSesion = c);
        return cliente;
    }

    public Optional<Cliente> getClienteSesion() {
        return Optional.ofNullable(clienteSesion);
    }

    public void cerrarSesion() {
        clienteSesion = null;
    }

    public Optional<Cliente> buscarClientePorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return clientes.stream().filter(c -> c.getEmail().equalsIgnoreCase(email.trim())).findFirst();
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    // ------------------------------------------------------------------ eventos y configuración

    public void agregarEvento(Evento evento) {
        eventos.add(evento);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public void agregarRegla(ReglaDeVenta regla) {
        reglas.add(regla);
    }

    public void agregarPromocion(Promocion promocion) {
        promociones.add(promocion);
    }

    public void agregarPaquete(PaqueteVIP paquete) {
        paquetes.add(paquete);
    }

    public List<PaqueteVIP> getPaquetes() {
        return Collections.unmodifiableList(paquetes);
    }

    public List<Funcion> buscarFunciones(String texto) {
        String filtro = texto == null ? "" : texto.trim().toLowerCase();
        return eventos.stream().flatMap(e -> e.getFuncionesDisponibles().stream())
                .filter(f -> f.getEvento().getNombre().toLowerCase().contains(filtro)
                        || f.getSede().getNombre().toLowerCase().contains(filtro))
                .sorted(Comparator.comparing(Funcion::getFechaHora))
                .toList();
    }

    // ------------------------------------------------------------------ flujo de compra

    public List<Asiento> consultarDisponibilidad(Funcion funcion, SectorNumerado sector, LocalDateTime ahora) {
        liberarReservasVencidas(ahora);
        return funcion.getAsientosLibres(sector);
    }

    public Compra iniciarCompra(Cliente cliente, String codigoPreventa, LocalDateTime ahora) {
        Compra compra = new Compra(cliente, codigoPreventa, ahora);
        compras.add(compra);
        return compra;
    }

    /** Reserva asientos de un sector numerado. Antes valida todas las reglas de venta. */
    public void reservarAsientos(Compra compra, Funcion funcion, SectorNumerado sector, List<Asiento> seleccion,
                                 LocalDateTime ahora) throws SeleccionInvalidaException {
        if (seleccion == null || seleccion.isEmpty()) {
            throw new SeleccionInvalidaException("Elegí al menos un asiento.");
        }
        liberarReservasVencidas(ahora);
        for (Asiento asiento : seleccion) {
            if (!funcion.estaDisponible(sector, asiento)) {
                throw new SeleccionInvalidaException("El asiento " + asiento + " ya no está disponible.");
            }
        }
        validarReglas(compra, funcion, sector, seleccion, ahora);
        for (Asiento asiento : seleccion) {
            compra.agregarEntrada(funcion, sector, asiento);
        }
    }

    /** Sobrecarga para sectores generales: se reserva una cantidad de lugares. */
    public void reservarAsientos(Compra compra, Funcion funcion, SectorGeneral sector, int cantidad,
                                 LocalDateTime ahora) throws SeleccionInvalidaException {
        if (cantidad <= 0) {
            throw new SeleccionInvalidaException("La cantidad debe ser mayor a cero.");
        }
        liberarReservasVencidas(ahora);
        if (funcion.lugaresLibres(sector) < cantidad) {
            throw new SeleccionInvalidaException("Quedan solo " + funcion.lugaresLibres(sector)
                    + " lugares en " + sector.getNombre() + ".");
        }
        validarReglas(compra, funcion, sector, List.of(), ahora);
        for (int i = 0; i < cantidad; i++) {
            compra.agregarEntrada(funcion, sector, null);
        }
    }

    private void validarReglas(Compra compra, Funcion funcion, Sector sector, List<Asiento> seleccion,
                               LocalDateTime ahora) throws SeleccionInvalidaException {
        for (ReglaDeVenta regla : reglas) {
            regla.validar(compra, funcion, sector, seleccion, ahora);
        }
    }

    public void agregarPaquete(Compra compra, PaqueteVIP paquete) {
        compra.agregarPaquete(paquete);
    }

    public void aplicarCodigoPromocional(Compra compra, String codigo) {
        Promocion promocion = promociones.stream().filter(p -> p.coincideCon(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El código promocional " + codigo + " no existe"));
        compra.aplicarPromocion(promocion);
    }

    /**
     * Confirma el pago. Si pasaron más de 5 minutos la reserva vence y se lanza ReservaVencidaException.
     * Si sale bien, emite la factura y la guarda en datos/facturas.
     */
    public Factura confirmarCompra(Compra compra, MedioPago medio, LocalDateTime ahora)
            throws ReservaVencidaException {
        String operacion = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        compra.confirmar(medio, operacion, ahora);
        Factura factura = new Factura(compra, ahora);
        facturas.add(factura);
        try {
            factura.guardar(directorioDatos.resolve("facturas"));
            guardarSecuenciaFactura();
        } catch (IOException e) {
            // La venta ya está hecha: si falla el archivo no se pierde la compra, solo se avisa.
            System.err.println("No se pudo guardar la factura " + factura.getNumero() + ": " + e.getMessage());
        }
        return factura;
    }

    private void cargarSecuenciaFactura() {
        Path archivo = directorioDatos.resolve("factura_seq.txt");
        try {
            if (Files.exists(archivo)) {
                int proximo = Integer.parseInt(Files.readString(archivo, StandardCharsets.UTF_8).trim());
                if (proximo >= 1) {
                    Factura.establecerSecuencia(proximo);
                }
                return;
            }
            int desdeDisco = inferirProximaSecuenciaDesdeFacturas();
            if (desdeDisco > 1) {
                Factura.establecerSecuencia(desdeDisco);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("No se pudo leer la secuencia de facturas: " + e.getMessage());
        }
    }

    private int inferirProximaSecuenciaDesdeFacturas() throws IOException {
        Path carpeta = directorioDatos.resolve("facturas");
        if (!Files.isDirectory(carpeta)) {
            return 1;
        }
        int max = 0;
        try (var archivos = Files.list(carpeta)) {
            for (Path path : archivos.filter(p -> p.getFileName().toString().startsWith("FAC-")).toList()) {
                Matcher m = PATRON_NUMERO_FACTURA.matcher(path.getFileName().toString());
                if (m.matches()) {
                    max = Math.max(max, Integer.parseInt(m.group(1)));
                }
            }
        }
        return max + 1;
    }

    private void guardarSecuenciaFactura() throws IOException {
        Path archivo = directorioDatos.resolve("factura_seq.txt");
        Files.writeString(archivo, Integer.toString(Factura.proximaSecuencia()), StandardCharsets.UTF_8);
    }

    public void cancelarCompra(Compra compra) {
        compra.cancelar();
    }

    /** Vence las compras reservadas que pasaron los 5 minutos y libera sus asientos. */
    public int liberarReservasVencidas(LocalDateTime ahora) {
        int vencidas = 0;
        for (Compra compra : compras) {
            if (compra.estaVencida(ahora)) {
                compra.vencer();
                vencidas++;
            }
        }
        return vencidas;
    }

    // ------------------------------------------------------------------ devoluciones

    public Devolucion devolverEntrada(String codigoEntrada, String motivo, LocalDateTime ahora)
            throws DevolucionNoPermitidaException {
        Entrada entrada = buscarEntrada(codigoEntrada)
                .orElseThrow(() -> new DevolucionNoPermitidaException("No existe la entrada " + codigoEntrada));
        if (!entrada.puedeDevolverse(ahora)) {
            throw new DevolucionNoPermitidaException("La entrada " + codigoEntrada + " no se puede devolver: "
                    + "tiene que estar pagada y faltar al menos " + Devolucion.HORAS_LIMITE
                    + " hs para la función.");
        }
        Devolucion devolucion = new Devolucion(entrada, motivo, ahora);
        entrada.devolver();
        devoluciones.add(devolucion);
        return devolucion;
    }

    public Optional<Entrada> buscarEntrada(String codigo) {
        return compras.stream().filter(c -> c.getEstado() == EstadoCompra.CONFIRMADA)
                .flatMap(c -> c.getEntradas().stream())
                .filter(e -> e.getCodigo().equalsIgnoreCase(codigo == null ? "" : codigo.trim()))
                .findFirst();
    }

    public List<Compra> getCompras() {
        return Collections.unmodifiableList(compras);
    }

    public List<Factura> getFacturas() {
        return Collections.unmodifiableList(facturas);
    }

    public List<Devolucion> getDevoluciones() {
        return Collections.unmodifiableList(devoluciones);
    }
}
