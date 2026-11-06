package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.VentaController;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.MedioPago;
import ar.edu.uade.poo.grupo3.tpo.modelo.eventos.Funcion;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.ReservaVencidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.SeleccionInvalidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Asiento;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorGeneral;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.SectorNumerado;
import ar.edu.uade.poo.grupo3.tpo.modelo.promociones.Promocion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Cliente;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Compra;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Factura;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.PaqueteVIP;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Pantalla de venta: se elige función y sector, se marcan asientos en el mapa, se reservan por
 * 5 minutos y se pagan. Al pagar se muestra la factura.
 * Es solo la vista: la compra en curso y el reloj de la reserva los lleva el VentaController.
 */
public class PanelVenta extends JPanel implements Pantalla {

    private static final Color LIBRE = Color.WHITE;
    private static final Color OCUPADO = Color.LIGHT_GRAY;
    private static final Color SELECCIONADO = new Color(0x3F51B5);
    private static final Color MIO = new Color(0x2E7D32);

    private final VentaController controlador;
    private final JComboBox<Funcion> funciones = new JComboBox<>();
    private final JComboBox<Sector> sectores = new JComboBox<>();
    private final JLabel clienteSesion = Estilo.subtitulo("Sin sesión");
    private final JTextField codigoPreventa = new JTextField(10);
    private final JLabel avisoPreventa = Estilo.subtitulo(" ");
    private final JPanel mapa = new JPanel();
    private final JSpinner cantidad = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));

    private final JLabel detalle = Estilo.subtitulo("Elegí tus asientos en el mapa.");
    private final JLabel precioUnitario = Estilo.subtitulo(" ");
    private final JCheckBox paqueteVip = new JCheckBox();
    private final JTextField codigoPromo = new JTextField(10);
    private final JButton aplicarPromo = new JButton("Aplicar");
    private final JLabel subtotal = new JLabel("—");
    private final JLabel descuento = new JLabel("—");
    private final JLabel cargos = new JLabel("—");
    private final JLabel total = new JLabel("—");
    private final JLabel reloj = new JLabel(" ");
    private final JComboBox<MedioPago> medioPago = new JComboBox<>(MedioPago.values());
    private final JButton reservar = new JButton("Reservar");
    private final JButton pagar = new JButton("Pagar");
    private final JButton cancelar = new JButton("Cancelar");
    private final Aviso aviso = new Aviso();

    private final Set<Asiento> seleccion = new LinkedHashSet<>();
    private final Timer timer = new Timer(1000, e -> tic());
    private boolean cargando;

    public PanelVenta(VentaController controlador) {
        super(new BorderLayout());
        this.controlador = controlador;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Venta de entradas",
                "Elegí la función y el sector, marcá tus asientos y reservalos: tenés 5 minutos para pagar."),
                BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(16, 0));
        centro.add(columnaMapa(), BorderLayout.CENTER);
        JPanel resumen = Estilo.tarjeta(columnaResumen());
        resumen.setPreferredSize(new Dimension(330, 0));
        centro.add(resumen, BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);
        add(Estilo.barraInferior(aviso), BorderLayout.SOUTH);

        funciones.addActionListener(e -> {
            if (!cargando) {
                cargarSectores();
            }
        });
        sectores.addActionListener(e -> {
            if (!cargando) {
                seleccion.clear();
                dibujarMapa();
            }
        });
        cantidad.addChangeListener(e -> actualizarResumen());
        paqueteVip.addActionListener(e -> actualizarResumen());
        reservar.addActionListener(e -> reservar());
        pagar.addActionListener(e -> pagar());
        cancelar.addActionListener(e -> cancelar());
        aplicarPromo.addActionListener(e -> aplicarPromocion());
    }

    // ------------------------------------------------------------------ armado de la pantalla

    private static JPanel campo(String etiqueta, JComponent componente) {
        JPanel campo = new JPanel(new BorderLayout(0, 4));
        campo.add(new JLabel(etiqueta), BorderLayout.NORTH);
        campo.add(componente, BorderLayout.CENTER);
        return campo;
    }

    private JComponent columnaMapa() {
        JPanel filtros = new JPanel(new GridLayout(2, 2, 10, 8));
        filtros.add(campo("Función", funciones));
        filtros.add(campo("Sector", sectores));
        filtros.add(campo("Cliente (sesión)", clienteSesion));
        filtros.add(campo("Código de preventa", codigoPreventa));

        JPanel arriba = new JPanel(new BorderLayout(0, 4));
        arriba.add(filtros, BorderLayout.CENTER);
        arriba.add(avisoPreventa, BorderLayout.SOUTH);

        JPanel envoltorio = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        envoltorio.add(mapa);
        JPanel contenedorMapa = new JPanel(new BorderLayout(0, 12));
        contenedorMapa.add(escenario(), BorderLayout.NORTH);
        contenedorMapa.add(envoltorio, BorderLayout.CENTER);
        contenedorMapa.add(leyenda(), BorderLayout.SOUTH);
        contenedorMapa.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel columna = new JPanel(new BorderLayout(0, 12));
        columna.add(arriba, BorderLayout.NORTH);
        columna.add(new JScrollPane(contenedorMapa), BorderLayout.CENTER);
        return Estilo.tarjeta(columna);
    }

    /** El escenario arriba del mapa, para orientarse: una etiqueta oscura centrada. */
    private static JLabel escenario() {
        JLabel escenario = new JLabel("ESCENARIO / PANTALLA", JLabel.CENTER);
        escenario.setOpaque(true);
        escenario.setBackground(Color.DARK_GRAY);
        escenario.setForeground(Color.WHITE);
        escenario.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        escenario.setPreferredSize(new Dimension(0, 34));
        return escenario;
    }

    private static JPanel leyenda() {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        leyenda.add(muestra(LIBRE, "Libre"));
        leyenda.add(muestra(SELECCIONADO, "Seleccionado"));
        leyenda.add(muestra(MIO, "Tu reserva"));
        leyenda.add(muestra(OCUPADO, "Ocupado"));
        return leyenda;
    }

    /** Un cuadradito del color indicado y su texto, para la leyenda del mapa. */
    private static JPanel muestra(Color color, String texto) {
        JLabel cuadrado = new JLabel();
        cuadrado.setOpaque(true);
        cuadrado.setBackground(color);
        cuadrado.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        cuadrado.setPreferredSize(new Dimension(14, 14));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panel.add(cuadrado);
        panel.add(Estilo.subtitulo(texto));
        return panel;
    }

    private JComponent columnaResumen() {
        JPanel promo = new JPanel(new BorderLayout(8, 0));
        promo.add(codigoPromo, BorderLayout.CENTER);
        promo.add(aplicarPromo, BorderLayout.EAST);

        total.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        reloj.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        reloj.setForeground(SELECCIONADO);

        JPanel botones = new JPanel(new GridLayout(1, 2, 8, 0));
        botones.add(cancelar);
        botones.add(reservar);

        JButton adelantar = new JButton("Demo: adelantar el reloj 5 minutos");
        adelantar.setToolTipText("Simula que pasaron 5 minutos para mostrar cómo vence la reserva");
        adelantar.addActionListener(e -> {
            controlador.adelantarReloj(5);
            tic();
        });

        return Estilo.columna(
                Estilo.seccion("Tu compra"),
                detalle,
                precioUnitario,
                paqueteVip,
                new JLabel("Código promocional"),
                promo,
                fila("Subtotal", subtotal),
                fila("Promoción", descuento),
                fila("Cargo por servicio", cargos),
                fila("Total", total),
                reloj,
                new JLabel("Medio de pago"),
                medioPago,
                botones,
                pagar,
                adelantar);
    }

    private static JPanel fila(String texto, JLabel valor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.add(Estilo.subtitulo(texto), BorderLayout.WEST);
        fila.add(valor, BorderLayout.EAST);
        return fila;
    }

    // ------------------------------------------------------------------ datos

    @Override
    public void alMostrar() {
        if (controlador.hayCompraEnCurso()) {
            dibujarMapa();
            return;
        }
        cargando = true;
        Object funcionAnterior = funciones.getSelectedItem();
        funciones.removeAllItems();
        controlador.funcionesDisponibles().forEach(funciones::addItem);
        if (funcionAnterior != null) {
            funciones.setSelectedItem(funcionAnterior);
        }
        actualizarClienteSesion();
        cargando = false;
        PaqueteVIP vip = controlador.paqueteVip();
        paqueteVip.setText(vip == null ? "Sin paquetes VIP"
                : "Sumar " + vip.getNombre() + " (" + Estilo.pesos(vip.getPrecio()) + ")");
        paqueteVip.setEnabled(vip != null);
        cargarSectores();
    }

    private void actualizarClienteSesion() {
        controlador.clienteSesion().ifPresentOrElse(
                c -> clienteSesion.setText(c.getNombre() + " · " + c.getEmail()),
                () -> clienteSesion.setText("Sin sesión — Clientes → Iniciar sesión"));
    }

    private Funcion funcion() {
        return (Funcion) funciones.getSelectedItem();
    }

    private Sector sector() {
        return (Sector) sectores.getSelectedItem();
    }

    private void cargarSectores() {
        cargando = true;
        sectores.removeAllItems();
        Funcion funcion = funcion();
        if (funcion != null) {
            funcion.getSede().getSectores().forEach(sectores::addItem);
            avisoPreventa.setText(controlador.estaEnPreventa(funcion)
                    ? "Esta función está en preventa: necesitás el código de acceso." : " ");
        }
        cargando = false;
        seleccion.clear();
        dibujarMapa();
    }

    // ------------------------------------------------------------------ mapa de asientos

    private void dibujarMapa() {
        mapa.removeAll();
        Funcion funcion = funcion();
        Sector sector = sector();
        if (funcion != null && sector != null) {
            controlador.liberarReservasVencidas();
            if (sector instanceof SectorNumerado numerado) {
                dibujarAsientos(funcion, numerado);
            } else {
                dibujarGeneral(funcion, (SectorGeneral) sector);
            }
        }
        mapa.revalidate();
        mapa.repaint();
        actualizarResumen();
    }

    /** Una grilla con una fila por letra: la etiqueta de la fila y un botón por asiento. */
    private void dibujarAsientos(Funcion funcion, SectorNumerado sector) {
        List<String> filas = sector.getFilas();
        int porFila = 0;
        for (String nombreFila : filas) {
            porFila = Math.max(porFila, sector.getAsientosDeFila(nombreFila).size());
        }
        mapa.setLayout(new GridLayout(filas.size(), porFila + 1, 3, 3));
        for (String nombreFila : filas) {
            mapa.add(new JLabel(nombreFila, JLabel.CENTER));
            List<Asiento> asientos = sector.getAsientosDeFila(nombreFila);
            for (Asiento asiento : asientos) {
                mapa.add(botonAsiento(asiento, estado(funcion, sector, asiento)));
            }
            for (int i = asientos.size(); i < porFila; i++) {
                mapa.add(new JLabel());
            }
        }
    }

    private void dibujarGeneral(Funcion funcion, SectorGeneral sector) {
        JPanel elegir = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        elegir.add(new JLabel("Cantidad de entradas"));
        cantidad.setEnabled(!controlador.hayCompraEnCurso());
        elegir.add(cantidad);
        JPanel panel = Estilo.columna(
                Estilo.seccion(sector.getNombre() + " · sector general (de pie)"),
                Estilo.subtitulo(Estilo.numero(funcion.lugaresLibres(sector)) + " de "
                        + Estilo.numero(sector.getCapacidad()) + " lugares libres · sin asiento asignado"),
                elegir);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        mapa.setLayout(new BorderLayout());
        mapa.add(panel, BorderLayout.CENTER);
    }

    private Color estado(Funcion funcion, Sector sector, Asiento asiento) {
        if (controlador.estaReservado(funcion, sector, asiento)) {
            return MIO;
        }
        if (seleccion.contains(asiento)) {
            return SELECCIONADO;
        }
        return funcion.estaDisponible(sector, asiento) ? LIBRE : OCUPADO;
    }

    /** Marca o desmarca un asiento (hasta 10 por compra) y vuelve a dibujar el mapa. */
    private void alternar(Asiento asiento) {
        if (!seleccion.remove(asiento)) {
            if (seleccion.size() >= 10) {
                aviso.error("Podés elegir hasta 10 asientos por compra.");
                return;
            }
            seleccion.add(asiento);
        }
        aviso.ocultar();
        dibujarMapa();
    }

    /** Un asiento del mapa: un botón chico con el color de su estado. Solo se puede clickear si está libre. */
    private JButton botonAsiento(Asiento asiento, Color color) {
        JButton boton = new JButton(String.valueOf(asiento.getNumero()));
        boton.setPreferredSize(new Dimension(32, 28));
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        boton.setFocusPainted(false);
        boton.setBackground(color);
        boton.setForeground(color == LIBRE || color == OCUPADO ? Color.BLACK : Color.WHITE);
        boton.setToolTipText("Fila " + asiento.getFila() + " · Asiento " + asiento.getNumero());
        boton.setEnabled(!controlador.hayCompraEnCurso() && (color == LIBRE || color == SELECCIONADO));
        boton.addActionListener(new OyenteAsiento(asiento));
        return boton;
    }

    /** Oyente con nombre (clase interna): cada botón del mapa sabe qué asiento representa. */
    private class OyenteAsiento implements ActionListener {

        private final Asiento asiento;

        OyenteAsiento(Asiento asiento) {
            this.asiento = asiento;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            alternar(asiento);
        }
    }

    // ------------------------------------------------------------------ resumen y acciones

    private void actualizarResumen() {
        Funcion funcion = funcion();
        Sector sector = sector();
        Compra compra = controlador.getCompra();
        PaqueteVIP vip = controlador.paqueteVip();
        boolean enCurso = compra != null;
        funciones.setEnabled(!enCurso);
        sectores.setEnabled(!enCurso);
        codigoPreventa.setEnabled(!enCurso);
        paqueteVip.setEnabled(!enCurso && vip != null);
        reservar.setEnabled(!enCurso && controlador.clienteSesion().isPresent());
        pagar.setEnabled(enCurso);
        codigoPromo.setEnabled(enCurso);
        aplicarPromo.setEnabled(enCurso);

        if (funcion == null || sector == null) {
            detalle.setText("No hay funciones disponibles.");
            return;
        }
        double unitario = funcion.calcularPrecio(sector);
        precioUnitario.setText(sector.getNombre() + " · " + Estilo.pesos(unitario) + " por entrada");

        if (enCurso) {
            StringBuilder texto = new StringBuilder("Reservado:");
            for (Entrada e : compra.getEntradasActivas()) {
                texto.append(' ').append(e.getAsiento() == null ? "general" : e.getAsiento());
            }
            detalle.setText(texto.toString());
            subtotal.setText(Estilo.pesos(compra.calcularSubtotal()));
            descuento.setText(compra.calcularDescuento() > 0 ? "- " + Estilo.pesos(compra.calcularDescuento()) : "—");
            cargos.setText(Estilo.pesos(compra.calcularCargos()));
            total.setText(Estilo.pesos(compra.calcularTotal()));
            return;
        }
        int entradas = sector instanceof SectorNumerado ? seleccion.size() : (Integer) cantidad.getValue();
        if (sector instanceof SectorNumerado) {
            detalle.setText(seleccion.isEmpty() ? "Elegí tus asientos en el mapa."
                    : "Elegidos: " + String.join(" ", seleccion.stream().map(Asiento::toString).toList()));
        } else {
            detalle.setText(entradas + " entrada(s) en " + sector.getNombre());
        }
        double estimado = entradas * unitario + (paqueteVip.isSelected() && vip != null ? vip.getPrecio() : 0);
        subtotal.setText(entradas == 0 ? "—" : Estilo.pesos(estimado));
        descuento.setText("—");
        cargos.setText(entradas == 0 ? "—" : "≈ " + Estilo.pesos(Math.round(estimado * Compra.CARGO_SERVICIO / 100)));
        total.setText(entradas == 0 ? "—" : "≈ " + Estilo.pesos(estimado + Math.round(estimado * Compra.CARGO_SERVICIO / 100)));
        reloj.setText(" ");
    }

    private void reservar() {
        Funcion funcion = funcion();
        Sector sector = sector();
        Cliente cliente = controlador.clienteSesion().orElse(null);
        if (funcion == null || sector == null) {
            aviso.error("Elegí función y sector.");
            return;
        }
        if (cliente == null) {
            aviso.error("Iniciá sesión en Clientes → Iniciar sesión antes de reservar.");
            return;
        }
        try {
            controlador.reservar(cliente, codigoPreventa.getText(), funcion, sector, new ArrayList<>(seleccion),
                    (Integer) cantidad.getValue(), paqueteVip.isSelected());
            seleccion.clear();
            timer.start();
            aviso.info("¡Listo! Tus lugares quedan reservados por " + Compra.MINUTOS_RESERVA
                    + " minutos. Elegí el medio de pago y pagá.");
        } catch (SeleccionInvalidaException | IllegalStateException ex) {
            aviso.error(ex.getMessage());
        }
        dibujarMapa();
        tic();
    }

    private void aplicarPromocion() {
        try {
            Promocion promocion = controlador.aplicarPromocion(codigoPromo.getText());
            aviso.exito("Promoción aplicada: " + promocion.getDescripcion());
        } catch (IllegalArgumentException ex) {
            aviso.error(ex.getMessage());
        }
        actualizarResumen();
    }

    private void pagar() {
        try {
            Factura factura = controlador.pagar((MedioPago) medioPago.getSelectedItem());
            terminarCompra();
            aviso.exito("Compra confirmada. Comprobante N° " + factura.getNumero() + ".");
            SwingUtilities.invokeLater(() ->
                    new FacturaDialog(SwingUtilities.getWindowAncestor(this), factura).setVisible(true));
        } catch (ReservaVencidaException ex) {
            terminarCompra();
            aviso.error(ex.getMessage());
        }
    }

    private void cancelar() {
        if (controlador.hayCompraEnCurso()) {
            controlador.cancelar();
            aviso.info("Reserva cancelada: los asientos volvieron a estar libres.");
        } else {
            aviso.ocultar();
        }
        terminarCompra();
    }

    /** Deja la pantalla como antes de reservar (la compra ya la cerró el controlador). */
    private void terminarCompra() {
        timer.stop();
        seleccion.clear();
        codigoPromo.setText("");
        reloj.setText(" ");
        dibujarMapa();
    }

    /** Cada segundo actualiza la cuenta regresiva; si llega a cero, la reserva vence y se liberan los asientos. */
    private void tic() {
        if (!controlador.hayCompraEnCurso()) {
            return;
        }
        if (controlador.verificarVencimiento()) {
            terminarCompra();
            aviso.error("Se terminaron los " + Compra.MINUTOS_RESERVA
                    + " minutos: la reserva venció y los asientos se liberaron.");
            return;
        }
        long segundos = controlador.segundosRestantes();
        reloj.setText(String.format("%02d:%02d  para pagar", segundos / 60, segundos % 60));
    }
}
