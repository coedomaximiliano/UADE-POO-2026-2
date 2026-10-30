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
    private JTabbedPane pantallas = new JTabbedPane(JTabbedPane.LEFT);

    public void abrirAgenda() {
        pantallas.setSelectedIndex(1);
    }

    public void abrirNuevoTurno(java.time.LocalDate fecha) {
        pantallas.setSelectedIndex(0);
        ((PanelCargarTurno) pantallas.getComponentAt(0)).seleccionarFecha(fecha);
    }

    public VentanaPrincipal(AplicacionClinica app) {
        super("Gestion de turnos de la clinica - Grupo 7");
        setName("ventanaClinica"); setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        setIconImage(new LogoClinica(64).imagen());
        JPanel marca = new JPanel(new BorderLayout(16, 0));
        marca.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        marca.add(new JLabel(new LogoClinica(48)), BorderLayout.WEST);
        JPanel nombre = new JPanel(new java.awt.GridLayout(2, 1, 0, 2));
        JLabel titulo = new JLabel("Clínica");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        nombre.add(titulo); nombre.add(new JLabel("Gestión de turnos"));
        marca.add(nombre, BorderLayout.CENTER);
        JLabel version = new JLabel("VERSIÓN DE PRÁCTICA");
        version.setForeground(EstiloClinica.SECUNDARIO);
        version.setFont(version.getFont().deriveFont(Font.BOLD, 12f));
        marca.add(version, BorderLayout.EAST); add(marca, BorderLayout.NORTH);
        pantallas.setName("pantallasClinica");
        pantallas.setUI(new BasicTabbedPaneUI() {
            protected void paintContentBorder(java.awt.Graphics g, int ubicacion, int seleccion) {
                // El contenido ya tiene su propio margen y fondo.
            }
            protected void paintTabBorder(java.awt.Graphics g, int ubicacion, int indice,
                    int x, int y, int ancho, int alto, boolean seleccionada) {
                if (seleccionada) {
                    g.setColor(EstiloClinica.PRIMARIO); g.fillRect(x, y, 4, alto);
                }
            }
        });
        pantallas.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        pantallas.addTab("Pedir turno", new IconoNavegacion("ticket", 30), new PanelCargarTurno(app), "Solicitar un turno");
        pantallas.addTab("Agenda", new IconoNavegacion("calendario", 30), new PanelAgenda(app), "Consultar el calendario y los turnos");
        pantallas.addTab("Pacientes", new PanelPacientes(app));
        pantallas.addTab("Médicos", new PanelMedicos(app));
        pantallas.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // Se refresca al entrar para ver los cambios de las otras pantallas.
                ((PanelPantalla) pantallas.getSelectedComponent()).actualizar();
                for (int i = 0; i < pantallas.getTabCount(); i++) {
                    pantallas.setForegroundAt(i, i == pantallas.getSelectedIndex() ? EstiloClinica.PRIMARIO : EstiloClinica.TEXTO);
                }
            }
        });
        // El menu abre las mismas pantallas que las pestañas laterales.
        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Ir a");
        String[] opciones = {"Pedir turno", "Agenda", "Pacientes", "Médicos"};
        for (int i = 0; i < opciones.length; i++) {
            final int pantalla = i;
            JMenuItem opcion = new JMenuItem(opciones[i]);
            opcion.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    pantallas.setSelectedIndex(pantalla);
                }
            });
            menu.add(opcion);
        }
        barra.add(menu);
        setJMenuBar(barra);
        add(pantallas, BorderLayout.CENTER);
        JLabel estado = new JLabel("Esta versión no guarda los cambios al cerrar. Los recordatorios son de prueba.");
        estado.setOpaque(true); estado.setBackground(new java.awt.Color(255, 247, 229));
        estado.setForeground(new java.awt.Color(120, 83, 30));
        estado.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        add(estado, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(VentanaPrincipal.this,
                        "Salir? Los datos de esta version se pierden al cerrar.", "Cerrar la clinica",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
        setMinimumSize(new Dimension(1000, 720)); setSize(1200, 820);
        // Uso el area disponible para centrar sin tapar la barra de tareas.
        java.awt.Rectangle area = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(getWidth(), area.width), Math.min(getHeight(), area.height));
        int x = area.x + Math.max(0, (area.width - getWidth()) / 2);
        int y = area.y + Math.max(0, (area.height - getHeight()) / 2);
        setLocation(x, y);
        pantallas.setSelectedIndex(0);
    }
}
