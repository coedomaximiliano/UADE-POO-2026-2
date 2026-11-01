package vista;

import aplicacion.AplicacionClinica;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class VentanaPrincipal extends JFrame {
    private javax.swing.Timer relojTimer;
    private PanelHoy panelHoy;
    private java.awt.TrayIcon iconoAvisos;
    private JTabbedPane pantallas = new JTabbedPane(JTabbedPane.LEFT);

    public void abrirAgendaResumen(String filtro, Integer id) {
        pantallas.setSelectedIndex(1);
        ((PanelAgenda) pantallas.getComponentAt(1)).mostrarResumen(filtro, id);
    }

    public void actualizarResumen() {
        if (panelHoy != null) panelHoy.actualizar();
    }

    public void abrirHoy() {
        pantallas.setSelectedIndex(4);
    }

    public void dispose() {
        if (iconoAvisos != null) {
            java.awt.SystemTray.getSystemTray().remove(iconoAvisos);
            iconoAvisos = null;
        }
        if (relojTimer != null) relojTimer.stop();
        if (panelHoy != null) panelHoy.detener();
        super.dispose();
    }

    public void abrirAgenda() {
        pantallas.setSelectedIndex(1);
    }

    public void abrirNuevoTurno(java.time.LocalDate fecha) {
        pantallas.setSelectedIndex(0);
        ((PanelCargarTurno) pantallas.getComponentAt(0)).seleccionarFecha(fecha);
    }

    public VentanaPrincipal(AplicacionClinica app) {
        super("Gestion de turnos - Grupo 7 - SQL Server");
        setName("ventanaClinica");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        setIconImage(new LogoClinica(64).imagen());
        JPanel marca = new JPanel(new BorderLayout(16, 0));
        marca.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        marca.add(new JLabel(new LogoClinica(48)), BorderLayout.WEST);
        JPanel nombre = new JPanel(new java.awt.GridLayout(2, 1, 0, 2));
        JLabel titulo = new JLabel("Clínica");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        nombre.add(titulo);
        nombre.add(new JLabel("Gestión de turnos"));
        marca.add(nombre, BorderLayout.CENTER);
        JPanel reloj = new JPanel(new java.awt.GridLayout(2, 1, 0, 3));
        JLabel fechaSistema = new JLabel("", SwingConstants.RIGHT),
                horaSistema = new JLabel("", SwingConstants.RIGHT);
        fechaSistema.setName("fechaSistema");
        horaSistema.setName("horaSistema");
        fechaSistema.setForeground(EstiloClinica.SECUNDARIO);
        horaSistema.setFont(horaSistema.getFont().deriveFont(Font.BOLD, 22f));
        reloj.add(fechaSistema);
        reloj.add(horaSistema);
        reloj.setOpaque(false);
        Runnable actualizarReloj =
                () -> {
                    java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
                    fechaSistema.setText(
                            ahora.format(
                                    java.time.format.DateTimeFormatter.ofPattern(
                                            "EEEE dd/MM/uuuu",
                                            java.util.Locale.forLanguageTag("es-AR"))));
                    horaSistema.setText(
                            ahora.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                };
        actualizarReloj.run();
        relojTimer = new javax.swing.Timer(1000, e -> actualizarReloj.run());
        relojTimer.start();
        marca.add(reloj, BorderLayout.EAST);
        add(marca, BorderLayout.NORTH);
        pantallas.setName("pantallasClinica");
        pantallas.setUI(
                new BasicTabbedPaneUI() {
                    protected void paintContentBorder(
                            java.awt.Graphics g, int ubicacion, int seleccion) {
                        // El contenido ya tiene su propio margen y fondo.
                    }

                    protected void paintTabBorder(
                            java.awt.Graphics g,
                            int ubicacion,
                            int indice,
                            int x,
                            int y,
                            int ancho,
                            int alto,
                            boolean seleccionada) {
                        if (seleccionada) {
                            g.setColor(EstiloClinica.PRIMARIO);
                            g.fillRect(x, y, 4, alto);
                        }
                    }
                });
        pantallas.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        pantallas.addTab(
                "Pedir turno",
                new IconoNavegacion("ticket", 30),
                new PanelCargarTurno(app),
                "Solicitar un turno");
        pantallas.addTab(
                "Agenda",
                new IconoNavegacion("calendario", 30),
                new PanelAgenda(app),
                "Consultar el calendario y los turnos");
        pantallas.addTab("Pacientes", new PanelPacientes(app));
        pantallas.addTab("Médicos", new PanelMedicos(app));
        panelHoy = new PanelHoy(app);
        pantallas.addTab(
                "Hoy", new IconoNavegacion("calendario", 30), panelHoy, "Turnos de hoy y avisos");
        pantallas.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        // Se refresca al entrar para ver los cambios de las otras pantallas.
                        try {
                            ((PanelPantalla) pantallas.getSelectedComponent()).actualizar();
                        } catch (IllegalArgumentException error) {
                            JOptionPane.showMessageDialog(
                                    VentanaPrincipal.this,
                                    error.getMessage(),
                                    "No se pudo consultar SQL Server",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        for (int i = 0; i < pantallas.getTabCount(); i++) {
                            pantallas.setForegroundAt(
                                    i,
                                    i == pantallas.getSelectedIndex()
                                            ? EstiloClinica.PRIMARIO
                                            : EstiloClinica.TEXTO);
                        }
                    }
                });
        // El menu abre las mismas pantallas que las pestañas laterales.
        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Ir a");
        String[] opciones = {"Pedir turno", "Agenda", "Pacientes", "Médicos", "Hoy"};
        for (int i = 0; i < opciones.length; i++) {
            final int pantalla = i;
            JMenuItem opcion = new JMenuItem(opciones[i]);
            opcion.addActionListener(
                    new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            pantallas.setSelectedIndex(pantalla);
                        }
                    });
            menu.add(opcion);
        }
        barra.add(menu);
        setJMenuBar(barra);
        add(pantallas, BorderLayout.CENTER);
        JPanel pie = new JPanel(new BorderLayout(14, 4));
        pie.setBackground(EstiloClinica.SUAVE);
        pie.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        JLabel resumen = new JLabel("Consultando turnos de hoy…");
        resumen.setName("resumenAvisosGlobal");
        JLabel estado =
                new JLabel(
                        "SQL Server · Correo simulado · Avisos activos mientras la app está abierta");
        estado.setFont(estado.getFont().deriveFont(10f));
        estado.setForeground(EstiloClinica.SECUNDARIO);
        JPanel textos = new JPanel(new java.awt.GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        textos.add(resumen);
        textos.add(estado);
        JButton verHoy = new JButton("Ver Hoy");
        EstiloClinica.boton(verHoy, false, false);
        verHoy.addActionListener(e -> abrirHoy());
        pie.add(textos, BorderLayout.CENTER);
        pie.add(verHoy, BorderLayout.EAST);
        add(pie, BorderLayout.SOUTH);
        panelHoy.setAlResumen(
                texto -> {
                    resumen.setText(texto);
                    resumen.setToolTipText(texto);
                });
        if (java.awt.SystemTray.isSupported()) {
            try {
                java.awt.PopupMenu opcionesAviso = new java.awt.PopupMenu();
                java.awt.MenuItem mostrarHoy = new java.awt.MenuItem("Ver turnos de hoy");
                Runnable mostrarResumen =
                        () -> {
                            setVisible(true);
                            setState(NORMAL);
                            toFront();
                            abrirHoy();
                        };
                mostrarHoy.addActionListener(e -> SwingUtilities.invokeLater(mostrarResumen));
                opcionesAviso.add(mostrarHoy);
                iconoAvisos =
                        new java.awt.TrayIcon(
                                new LogoClinica(32).imagen(),
                                "Turnos de la clínica",
                                opcionesAviso);
                iconoAvisos.setImageAutoSize(true);
                iconoAvisos.addActionListener(e -> SwingUtilities.invokeLater(mostrarResumen));
                java.awt.SystemTray.getSystemTray().add(iconoAvisos);
                panelHoy.setAlNotificacion(
                        texto -> {
                            java.awt.Window activa =
                                    java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager()
                                            .getActiveWindow();
                            boolean aqui = false;
                            while (activa != null) {
                                if (activa == this) aqui = true;
                                activa = activa.getOwner();
                            }
                            if (iconoAvisos != null && !aqui)
                                iconoAvisos.displayMessage(
                                        "Turnos de la clínica",
                                        texto,
                                        java.awt.TrayIcon.MessageType.INFO);
                        });
            } catch (java.awt.AWTException error) {
                iconoAvisos = null;
            }
        }
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (JOptionPane.showConfirmDialog(
                                        VentanaPrincipal.this,
                                        "¿Cerrar la aplicación? Los cambios guardados permanecen en SQL Server.",
                                        "Cerrar la clinica",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE)
                                == JOptionPane.YES_OPTION) {
                            dispose();
                        }
                    }
                });
        setMinimumSize(new Dimension(1000, 720));
        setSize(1200, 820);
        // Uso el area disponible para centrar sin tapar la barra de tareas.
        java.awt.Rectangle area =
                java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(getWidth(), area.width), Math.min(getHeight(), area.height));
        int x = area.x + Math.max(0, (area.width - getWidth()) / 2);
        int y = area.y + Math.max(0, (area.height - getHeight()) / 2);
        setLocation(x, y);
        pantallas.setSelectedIndex(4);
    }
}
