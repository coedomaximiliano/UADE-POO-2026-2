package vista;

import logica.ComprobanteTurnoPdf;

import modelo.Turno;

import java.awt.*;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ConfirmacionTurno extends JDialog {
    private Turno turno;
    private JButton aceptar;

    public ConfirmacionTurno(Window ventana, Turno turno) {
        super(ventana, "Turno confirmado", ModalityType.APPLICATION_MODAL);
        this.turno = turno;
        setName("confirmacionTurno");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel contenido = new JPanel(new BorderLayout(0, 22));
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(26, 28, 22, 28));
        JPanel encabezado = new JPanel(new BorderLayout(16, 0));
        encabezado.setOpaque(false);
        encabezado.add(new JLabel(new LogoClinica(54)), BorderLayout.WEST);
        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 5));
        titulos.setOpaque(false);
        JLabel titulo =
                new JLabel(
                        turno.getEstado() == modelo.EstadoTurno.PENDIENTE
                                ? "¡Tu turno está confirmado!"
                                : "Detalle de tu turno");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 23f));
        titulo.setForeground(EstiloClinica.TEXTO);
        JLabel subtitulo =
                new JLabel("Reserva N° " + turno.getId() + " · " + turno.getEstado().name());
        subtitulo.setForeground(EstiloClinica.SECUNDARIO);
        titulos.add(titulo);
        titulos.add(subtitulo);
        encabezado.add(titulos, BorderLayout.CENTER);
        contenido.add(encabezado, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        JPanel cita = new JPanel(new GridLayout(1, 2, 20, 0));
        cita.setBackground(EstiloClinica.SUAVE);
        cita.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        cita.add(
                destacado(
                        "FECHA",
                        turno.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
        cita.add(destacado("HORA", turno.getHora().format(DateTimeFormatter.ofPattern("HH:mm"))));
        centro.add(cita, BorderLayout.NORTH);
        JPanel detalle = new JPanel(new GridLayout(0, 1, 0, 12));
        detalle.setOpaque(false);
        detalle.add(
                dato(
                        "Paciente",
                        turno.getPaciente().getNombre()
                                + " · DNI "
                                + turno.getPaciente().getDni()));
        detalle.add(
                dato(
                        "Médico",
                        turno.getMedico().getNombre() + " · " + turno.getMedico().getMatricula()));
        detalle.add(dato("Especialidad", turno.getMedico().getEspecialidad()));
        detalle.add(dato("Consultorio", turno.getMedico().getConsultorio()));
        centro.add(detalle, BorderLayout.CENTER);
        JLabel aviso = new JLabel("El recordatorio por correo continúa siendo de prueba.");
        aviso.setForeground(EstiloClinica.SECUNDARIO);
        aviso.setFont(aviso.getFont().deriveFont(11f));
        centro.add(aviso, BorderLayout.SOUTH);
        contenido.add(centro, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        JButton cerrar = new JButton("Cerrar");
        cerrar.setName("cerrarConfirmacion");
        EstiloClinica.boton(cerrar, false, false);
        cerrar.addActionListener(e -> dispose());
        aceptar = new JButton("Aceptar y generar PDF");
        aceptar.setName("generarPdfTurno");
        EstiloClinica.boton(aceptar, true, false);
        aceptar.addActionListener(e -> guardarPdf());
        botones.add(cerrar);
        botones.add(aceptar);
        contenido.add(botones, BorderLayout.SOUTH);
        setContentPane(contenido);
        getRootPane().setDefaultButton(aceptar);
        setMinimumSize(new Dimension(640, 540));
        setSize(690, 590);
        setLocationRelativeTo(ventana);
    }

    private JPanel destacado(String titulo, String valor) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 6));
        p.setOpaque(false);
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setForeground(EstiloClinica.PRIMARIO);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 11f));
        JLabel dato = new JLabel(valor);
        dato.setForeground(EstiloClinica.TEXTO);
        dato.setFont(dato.getFont().deriveFont(Font.BOLD, 24f));
        p.add(etiqueta);
        p.add(dato);
        return p;
    }

    private JLabel dato(String etiqueta, String valor) {
        String seguro = valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return new JLabel(
                "<html><span style='color:#647582'>"
                        + etiqueta
                        + "</span><br><b>"
                        + seguro
                        + "</b></html>");
    }

    private void guardarPdf() {
        JFileChooser elegir = new JFileChooser();
        elegir.setDialogTitle("Guardar comprobante del turno");
        elegir.setAcceptAllFileFilterUsed(false);
        elegir.setFileFilter(new FileNameExtensionFilter("Comprobante PDF (*.pdf)", "pdf"));
        elegir.setSelectedFile(
                new java.io.File("Turno-" + turno.getId() + "-" + turno.getFecha() + ".pdf"));
        if (elegir.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        Path archivo = elegir.getSelectedFile().toPath();
        if (!archivo.getFileName().toString().toLowerCase(java.util.Locale.ROOT).endsWith(".pdf"))
            archivo = archivo.resolveSibling(archivo.getFileName() + ".pdf");
        if (java.nio.file.Files.exists(archivo)
                && JOptionPane.showConfirmDialog(
                                this,
                                "Ese PDF ya existe. ¿Reemplazarlo?",
                                "Guardar comprobante",
                                JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        aceptar.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            new ComprobanteTurnoPdf().generar(turno, archivo, new LogoClinica(160).imagen());
            dispose();
            try {
                if (Desktop.isDesktopSupported()
                        && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
                    Desktop.getDesktop().open(archivo.toFile());
                else
                    JOptionPane.showMessageDialog(
                            getOwner(),
                            "PDF guardado en:\n" + archivo.toAbsolutePath(),
                            "Comprobante guardado",
                            JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception abrir) {
                JOptionPane.showMessageDialog(
                        getOwner(),
                        "El PDF quedó guardado en:\n" + archivo.toAbsolutePath(),
                        "Comprobante guardado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El turno sigue confirmado. No se pudo guardar el PDF.\nElegí otra carpeta o cerrá el archivo si está abierto.",
                    "No se pudo generar el comprobante",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            aceptar.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
