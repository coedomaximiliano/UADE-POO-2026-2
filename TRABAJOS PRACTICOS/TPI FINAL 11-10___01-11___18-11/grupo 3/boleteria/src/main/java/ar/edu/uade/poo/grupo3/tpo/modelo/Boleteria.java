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
import ar.edu.uade.poo.grupo3.tpo.modelo.persistencia.Repositorio;
import ar.edu.uade.poo.grupo3.tpo.modelo.persistencia.SedeRepositorioArchivo;
import ar.edu.uade.poo.grupo3.tpo.modelo.promociones.Promocion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Cliente;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Devolucion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Factura;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Fachada del modelo: patrón Singleton + Controlador (GRASP), única puerta de entrada a las operaciones del sistema.
 * En términos de MVC es parte del modelo; los controladores de MVC (SedeController, VentaController)
 * le hablan a la Boleteria, nunca directo al resto del dominio.
 */
public final class Boleteria {

    private static Boleteria instancia;

    private final Path directorioDatos;
    private final Repositorio<Sede> repoSedes;
    private final Repositorio<Cliente> repoClientes;

    private final List<Sede> sedes = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<Factura> facturas = new ArrayList<>();
    private final List<Devolucion> devoluciones = new ArrayList<>();
    private final List<Promocion> promociones = new ArrayList<>();

    private Boleteria(Path directorioDatos) {
        this.directorioDatos = directorioDatos;
        this.repoSedes = new SedeRepositorioArchivo(directorioDatos);
        this.repoClientes = new ClienteRepositorioArchivo(directorioDatos);
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
        cargarSecuenciaFactura();
    }

    public void guardarSedes() throws IOException {
        repoSedes.guardarTodos(sedes);
    }

    public void guardarClientes() throws IOException {
        repoClientes.guardarTodos(clientes);
    }

    public Path getDirectorioDatos() {
        return directorioDatos;
    }

    // ------------------------------------------------------------------ sedes (ABM)

    public List<Sede> getSedes() {
        return Collections.unmodifiableList(sedes);
    }

    /** Devuelve null si no existe. */
    public Sede buscarSede(String nombre) {
        for (Sede s : sedes) {
            if (s.getNombre().equalsIgnoreCase(nombre.trim())) {
                return s;
            }
        }
        return null;
    }

    public void agregarSede(Sede sede) {
        if (sede == null) {
            throw new IllegalArgumentException("La sede no puede ser nula");
        }
        if (buscarSede(sede.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe una sede llamada " + sede.getNombre());
        }
        if (sede.getSectores().isEmpty()) {
            throw new IllegalArgumentException("La sede necesita al menos un sector");
        }
        sedes.add(sede);
    }

    public void modificarSede(Sede sede, String nuevoNombre, String nuevaDireccion) {
        Sede otra = buscarSede(nuevoNombre);
        if (otra != null && otra != sede) {
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
        List<Funcion> activas = new ArrayList<>();
        for (Evento e : eventos) {
            for (Funcion f : e.getFunciones()) {
                if (f.getSede() == sede
                        && (f.getEstado() == EstadoFuncion.PROGRAMADA || f.getEstado() == EstadoFuncion.AGOTADA)) {
                    activas.add(f);
                }
            }
        }
        return activas;
    }

    private void verificarSinFunciones(Sede sede, String accion) {
        int cantidad = getFuncionesActivas(sede).size();
        if (cantidad > 0) {
            throw new IllegalStateException("No se puede " + accion + ": " + sede.getNombre() + " tiene "
                    + cantidad + " función(es) programada(s).");
        }
    }

    // ------------------------------------------------------------------ clientes

    public Cliente registrarCliente(String dni, String nombre, String email) {
        Cliente cliente = new Cliente(dni, nombre, email);
        if (buscarClientePorEmail(cliente.getEmail()) != null) {
            throw new IllegalArgumentException("Ya hay una cuenta con el email " + cliente.getEmail());
        }
        for (Cliente c : clientes) {
            if (c.getDni().equals(cliente.getDni())) {
                throw new IllegalArgumentException("Ya hay una cuenta con el DNI " + cliente.getDni());
            }
        }
        clientes.add(cliente);
        return cliente;
    }

    /** Devuelve null si no existe. */
    public Cliente buscarClientePorEmail(String email) {
        if (email == null) {
            return null;
        }
        for (Cliente c : clientes) {
            if (c.getEmail().equalsIgnoreCase(email.trim())) {
                return c;
            }
        }
        return null;
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

    public void agregarPromocion(Promocion promocion) {
        promociones.add(promocion);
    }

    public List<Funcion> buscarFunciones(String texto) {
        String filtro = texto == null ? "" : texto.trim().toLowerCase();
        List<Funcion> encontradas = new ArrayList<>();
        for (Evento e : eventos) {
            for (Funcion f : e.getFuncionesDisponibles()) {
                if (f.getEvento().getNombre().toLowerCase().contains(filtro)
                        || f.getSede().getNombre().toLowerCase().contains(filtro)) {
                    encontradas.add(f);
                }
            }
        }
        encontradas.sort(new Comparator<Funcion>() {
            @Override
            public int compare(Funcion a, Funcion b) {
                return a.getFechaHora().compareTo(b.getFechaHora());
            }
        });
        return encontradas;
    }

    // ------------------------------------------------------------------ flujo de compra

    public List<Asiento> consultarDisponibilidad(Funcion funcion, SectorNumerado sector, LocalDateTime ahora) {
        liberarReservasVencidas(ahora);
        return funcion.getAsientosLibres(sector);
    }

    public Compra iniciarCompra(Cliente cliente, LocalDateTime ahora) {
        Compra compra = new Compra(cliente, ahora);
        compras.add(compra);
        return compra;
    }

    /** Reserva asientos de un sector numerado. */
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
        for (int i = 0; i < cantidad; i++) {
            compra.agregarEntrada(funcion, sector, null);
        }
    }

    public void aplicarCodigoPromocional(Compra compra, String codigo) {
        for (Promocion promocion : promociones) {
            if (promocion.coincideCon(codigo)) {
                compra.aplicarPromocion(promocion);
                return;
            }
        }
        throw new IllegalArgumentException("El código promocional " + codigo + " no existe");
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
        } catch (IOException e) {
            // La venta ya está hecha: si falla el archivo no se pierde la compra, solo se avisa.
            System.err.println("No se pudo guardar la factura " + factura.getNumero() + ": " + e.getMessage());
        }
        return factura;
    }

    /** Sigue la numeración de facturas: si ya hay N guardadas en datos/facturas, la próxima es la N+1. */
    private void cargarSecuenciaFactura() throws IOException {
        Path carpeta = directorioDatos.resolve("facturas");
        if (Files.isDirectory(carpeta)) {
            String[] archivos = carpeta.toFile().list();
            Factura.establecerSecuencia(archivos.length + 1);
        }
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
        Entrada entrada = buscarEntrada(codigoEntrada);
        if (entrada == null) {
            throw new DevolucionNoPermitidaException("No existe la entrada " + codigoEntrada);
        }
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

    /** Devuelve null si no existe. */
    public Entrada buscarEntrada(String codigo) {
        String buscado = codigo == null ? "" : codigo.trim();
        for (Compra c : compras) {
            if (c.getEstado() == EstadoCompra.CONFIRMADA) {
                for (Entrada e : c.getEntradas()) {
                    if (e.getCodigo().equalsIgnoreCase(buscado)) {
                        return e;
                    }
                }
            }
        }
        return null;
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
