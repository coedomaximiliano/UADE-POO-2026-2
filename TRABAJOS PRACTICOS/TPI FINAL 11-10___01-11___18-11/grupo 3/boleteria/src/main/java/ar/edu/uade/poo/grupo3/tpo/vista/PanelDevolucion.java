package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.VentaController;
import ar.edu.uade.poo.grupo3.tpo.modelo.excepcion.DevolucionNoPermitidaException;
import ar.edu.uade.poo.grupo3.tpo.modelo.enums.EstadoEntrada;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Devolucion;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Entrada;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Optional;

/**
 * Pantalla para validar entradas y gestionar su devolución.
 */
public class PanelDevolucion extends JPanel implements Pantalla {

    private final VentaController controlador;
    private final JLabel detalle = Estilo.subtitulo("Verificá una entrada para ver el reembolso.");
    private final JLabel ubicacion = Estilo.subtitulo(" ");
    private final JTextField codigoEntrada = new JTextField(10);
    private final JTextArea motivoDevolucion = new JTextArea(4, 20);
    private final JLabel reembolso = new JLabel("—");
    private final JButton devolver = new JButton("Efectuar devolución");
    private final JButton verificarEntrada = new JButton("Verificar entrada");
    private final Aviso aviso = new Aviso();

    public PanelDevolucion(VentaController controlador) {
        super(new BorderLayout());
        this.controlador = controlador;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Devolución de entradas",
                "Verificá la entrada y consultá el monto a reembolsar antes de confirmar."),
                BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(16, 0));
        centro.add(columnaMapa(), BorderLayout.CENTER);
        JPanel resumen = Estilo.tarjeta(columnaResumen());
        resumen.setPreferredSize(new Dimension(330, 0));
        centro.add(resumen, BorderLayout.EAST);
        add(centro, BorderLayout.CENTER);
        add(Estilo.barraInferior(aviso), BorderLayout.SOUTH);

        verificarEntrada.addActionListener(e -> verificarEntrada());
        codigoEntrada.addActionListener(e -> verificarEntrada());
        devolver.addActionListener(e -> efectuarDevolucion());
        devolver.setEnabled(false);
    }

    // ------------------------------------------------------------------ armado de la pantalla

    private JComponent columnaMapa() {
        JPanel codigo = new JPanel(new BorderLayout(8, 0));
        codigo.add(codigoEntrada, BorderLayout.CENTER);
        codigo.add(verificarEntrada, BorderLayout.EAST);

        JPanel campoCodigo = new JPanel(new BorderLayout(0, 6));
        campoCodigo.add(new JLabel("Código de entrada"), BorderLayout.NORTH);
        campoCodigo.add(codigo, BorderLayout.CENTER);

        motivoDevolucion.setLineWrap(true);
        motivoDevolucion.setWrapStyleWord(true);
        motivoDevolucion.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane motivoScroll = new JScrollPane(motivoDevolucion);
        motivoScroll.setBorder(BorderFactory.createLineBorder(Estilo.BORDE));
        motivoScroll.setPreferredSize(new Dimension(0, 180));

        JPanel campoMotivo = new JPanel(new BorderLayout(0, 6));
        campoMotivo.add(new JLabel("Motivo de la devolución"), BorderLayout.NORTH);
        campoMotivo.add(motivoScroll, BorderLayout.CENTER);

        JPanel campos = new JPanel(new BorderLayout(0, 16));
        campos.add(campoCodigo, BorderLayout.NORTH);
        campos.add(campoMotivo, BorderLayout.CENTER);

        JPanel columna = new JPanel(new BorderLayout(0, 16));
        columna.add(Estilo.seccion("Datos de la entrada"), BorderLayout.NORTH);
        columna.add(campos, BorderLayout.CENTER);
        return Estilo.tarjeta(columna);
    }


    private JComponent columnaResumen() {
        reembolso.setFont(Estilo.NUMERO_GRANDE);
        return Estilo.columna(
                Estilo.seccion("Tu devolución"),
                detalle,
                ubicacion,
                fila("Monto a reembolsar", reembolso),
                devolver);
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
        codigoEntrada.setText("");
        motivoDevolucion.setText("");
        limpiarResumen();
        aviso.ocultar();
        codigoEntrada.requestFocusInWindow();
    }

    private void limpiarResumen() {
        detalle.setText("Verificá una entrada para ver el reembolso.");
        ubicacion.setText(" ");
        reembolso.setText("—");
        devolver.setEnabled(false);
    }

    /** Busca la entrada y muestra qué se devolvería. Solo habilita el botón si la devolución es posible. */
    private void verificarEntrada() {
        limpiarResumen();
        Optional<Entrada> buscada = controlador.buscarEntrada(codigoEntrada.getText());
        if (buscada.isEmpty()) {
            aviso.error("No existe ninguna entrada pagada con el código " + codigoEntrada.getText().trim() + ".");
            return;
        }
        Entrada entrada = buscada.get();
        detalle.setText(entrada.getCodigo() + " · " + entrada.getFuncion().getEvento().getNombre());
        ubicacion.setText(entrada.getUbicacion());
        reembolso.setText(Estilo.pesos(entrada.getValorPagado()));
        if (entrada.getEstado() == EstadoEntrada.DEVUELTA) {
            aviso.error("La entrada " + entrada.getCodigo() + " ya fue devuelta.");
        } else if (!controlador.puedeDevolverse(entrada)) {
            aviso.error("La entrada " + entrada.getCodigo() + " no se puede devolver: faltan menos de "
                    + Devolucion.HORAS_LIMITE + " hs para la función.");
        } else {
            aviso.info("Entrada válida. Se reintegra lo pagado por la entrada, sin el cargo por servicio.");
            devolver.setEnabled(true);
        }
    }

    private void efectuarDevolucion() {
        try {
            Devolucion devolucion = controlador.devolverEntrada(codigoEntrada.getText(), motivoDevolucion.getText());
            alMostrar();
            aviso.exito("Devolución registrada: se reintegran " + Estilo.pesos(devolucion.getMontoReintegro())
                    + " por la entrada " + devolucion.getEntrada().getCodigo() + ". El asiento vuelve a la venta.");
        } catch (DevolucionNoPermitidaException ex) {
            aviso.error(ex.getMessage());
            devolver.setEnabled(false);
        }
    }
}
