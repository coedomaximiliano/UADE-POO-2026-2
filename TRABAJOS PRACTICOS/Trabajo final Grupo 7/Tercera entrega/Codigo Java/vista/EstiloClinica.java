package vista;

import modelo.EstadoTurno;
import modelo.Medico;
import modelo.Paciente;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class EstiloClinica {
    // La paleta esta en un solo lugar para no repetir colores en cada pantalla.
    public static final Color PRIMARIO = Color.decode("#0F766E");
    public static final Color FONDO = Color.decode("#F3F7F8");
    public static final Color TEXTO = Color.decode("#17324D");
    public static final Color SECUNDARIO = Color.decode("#526475");
    public static final Color BORDE = Color.decode("#CBD5E1");
    public static final Color PELIGRO = Color.decode("#B91C1C");
    public static final Color SUAVE = Color.decode("#E4F2EF");

    public static void configurar() {
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
        String[] componentes = {"Label", "Button", "TextField", "TextArea", "ComboBox", "Table",
                "TableHeader", "TabbedPane", "OptionPane", "List"};
        for (String componente : componentes) { UIManager.put(componente + ".font", fuente); }
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("TextField.foreground", TEXTO);
        UIManager.put("TextArea.foreground", TEXTO);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("Button.disabledText", Color.decode("#657687"));
        UIManager.put("TabbedPane.tabInsets", new Insets(14, 17, 14, 17));
        UIManager.put("TabbedPane.selected", SUAVE);
        UIManager.put("TabbedPane.background", FONDO);
        UIManager.put("TabbedPane.foreground", TEXTO);
        UIManager.put("TabbedPane.contentAreaColor", FONDO);
        UIManager.put("TabbedPane.focus", PRIMARIO);
    }

    public static Border bordeCampo() {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(7, 10, 7, 10));
    }

    public static void boton(JButton boton, boolean principal, boolean peligro) {
        // El estilo nativo de Windows ignora algunos colores. Este respeta la paleta.
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            protected void paintFocus(Graphics g, AbstractButton b, Rectangle vista, Rectangle texto, Rectangle icono) {
                g.setColor(b.getForeground());
                g.drawRect(5, 5, b.getWidth() - 11, b.getHeight() - 11);
            }
        });
        boton.setOpaque(true); boton.setContentAreaFilled(true);
        boton.setBackground(principal ? PRIMARIO : Color.WHITE);
        boton.setForeground(principal ? Color.WHITE : peligro ? PELIGRO : TEXTO);
        boton.setFont(boton.getFont().deriveFont(Font.BOLD, 14f));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(peligro ? PELIGRO : principal ? PRIMARIO : BORDE),
                BorderFactory.createEmptyBorder(9, 16, 9, 16)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addPropertyChangeListener("enabled", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evento) {
                boton.setBackground(boton.isEnabled() ? principal ? PRIMARIO : Color.WHITE : Color.decode("#E4E9ED"));
                boton.setForeground(boton.isEnabled() ? principal ? Color.WHITE : peligro ? PELIGRO : TEXTO : SECUNDARIO);
            }
        });
    }

    public static void combo(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(240, 40));
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                String texto = valor == null ? "" : valor.toString();
                if (valor instanceof Medico) {
                    Medico medico = (Medico) valor;
                    texto = medico.getNombre() + " · " + medico.getMatricula();
                } else if (valor instanceof Paciente) {
                    Paciente paciente = (Paciente) valor;
                    texto = paciente.getNombre() + " · " + paciente.getDni();
                }
                setText(texto); setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                return this;
            }
        });
    }

    public static String nombreEstado(EstadoTurno estado) {
        if (estado == EstadoTurno.PENDIENTE) { return "Pendiente"; }
        if (estado == EstadoTurno.ATENDIDO) { return "Atendido"; }
        return "Cancelado";
    }

    public static DefaultTableCellRenderer celdas() {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
                    boolean foco, int fila, int columna) {
                super.getTableCellRendererComponent(tabla, valor, seleccionado, foco, fila, columna);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                setHorizontalAlignment(LEFT);
                setForeground(TEXTO);
                setBackground(seleccionado ? Color.decode("#D4EAE6") : fila % 2 == 0 ? Color.WHITE : Color.decode("#F7FAFB"));
                if (valor instanceof EstadoTurno) {
                    EstadoTurno estado = (EstadoTurno) valor;
                    setText(nombreEstado(estado));
                    if (!seleccionado) {
                        setBackground(estado == EstadoTurno.PENDIENTE ? Color.decode("#FEF3C7")
                                : estado == EstadoTurno.ATENDIDO ? Color.decode("#DCFCE7") : Color.decode("#FEE2E2"));
                        setForeground(estado == EstadoTurno.PENDIENTE ? Color.decode("#92400E")
                                : estado == EstadoTurno.ATENDIDO ? Color.decode("#166534") : PELIGRO);
                    }
                }
                return this;
            }
        };
    }
}
