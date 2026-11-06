package ar.edu.uade.poo.grupo3.tpo.vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Locale;

/** Fuentes, formatos y componentes comunes, para que todas las pantallas se armen de la misma manera. */
public final class Estilo {

    public static final Font TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    public static final Font SECCION = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font NUMERO_GRANDE = new Font(Font.SANS_SERIF, Font.BOLD, 26);
    public static final Color GRIS = new Color(0x606060);
    public static final Color BORDE = new Color(0xC0C0C0);

    private static final NumberFormat NUMEROS = NumberFormat.getIntegerInstance(new Locale("es", "AR"));

    private Estilo() {
    }

    /** Look & feel multiplataforma de Java, para que la aplicación se vea igual en Mac y en Windows. */
    public static void aplicarGlobal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el look & feel: " + e.getMessage());
        }
    }

    public static String numero(long valor) {
        return NUMEROS.format(valor);
    }

    public static String pesos(double valor) {
        return "$ " + NUMEROS.format(Math.round(valor));
    }

    // ------------------------------------------------------------------ textos

    public static JLabel titulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(TITULO);
        return label;
    }

    public static JLabel seccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(SECCION);
        return label;
    }

    public static JLabel subtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(GRIS);
        return label;
    }

    /** Encabezado de cada pantalla: el título y una línea que explica qué se hace ahí. */
    public static JPanel encabezado(String titulo, String subtitulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(titulo(titulo), BorderLayout.NORTH);
        panel.add(subtitulo(subtitulo), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        return panel;
    }

    // ------------------------------------------------------------------ contenedores

    /** Panel con borde y margen interno, para agrupar una parte de la pantalla. */
    public static JPanel tarjeta(JComponent contenido) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    /** Componentes apilados de arriba hacia abajo (una columna de GridLayout) que no se estiran al fondo. */
    public static JPanel columna(JComponent... componentes) {
        JPanel grilla = new JPanel(new GridLayout(0, 1, 0, 6));
        for (JComponent componente : componentes) {
            grilla.add(componente);
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(grilla, BorderLayout.NORTH);
        return panel;
    }

    /** Barra de abajo de cada pantalla: el aviso a la izquierda y los botones a la derecha. */
    public static JPanel barraInferior(Aviso aviso, JButton... botones) {
        JPanel barra = new JPanel(new BorderLayout(12, 0));
        barra.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        barra.add(aviso, BorderLayout.CENTER);
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        for (JButton boton : botones) {
            derecha.add(boton);
        }
        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    // ------------------------------------------------------------------ tablas

    /** Modelo de tabla de solo lectura: las celdas no se editan con doble click. */
    public static DefaultTableModel modeloTabla(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    public static JTable tabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(26);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        return tabla;
    }

    /** Anchos preferidos de las columnas (en píxeles), en el orden de las columnas. */
    public static void anchos(JTable tabla, int... anchos) {
        for (int i = 0; i < anchos.length && i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    public static JScrollPane scroll(JComponent componente) {
        return new JScrollPane(componente);
    }
}
