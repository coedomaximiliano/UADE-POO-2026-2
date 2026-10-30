package vista;

import aplicacion.AplicacionClinica;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public abstract class PanelPantalla extends JPanel {
    protected AplicacionClinica app;
    protected JPanel cabecera;

    public PanelPantalla(AplicacionClinica app, String titulo, String ayuda) {
        this.app = app;
        setLayout(new BorderLayout(12, 16));
        setBackground(EstiloClinica.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 20, 24));
        cabecera = new JPanel(new BorderLayout(0, 8));
        cabecera.setOpaque(false);
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 26f));
        cabecera.add(etiqueta, BorderLayout.NORTH);
        JLabel descripcion = new JLabel("<html>" + ayuda + "</html>");
        descripcion.setForeground(EstiloClinica.SECUNDARIO);
        cabecera.add(descripcion, BorderLayout.CENTER);
        add(cabecera, BorderLayout.NORTH);
    }

    protected JTextField campo(String nombre) {
        JTextField campo = new JTextField(22);
        campo.setName(nombre);
        campo.setBorder(EstiloClinica.bordeCampo());
        return campo;
    }

    protected JButton boton(String texto, String nombre, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setName(nombre);
        boton.addActionListener(accion);
        boolean principal = nombre.startsWith("guardar") || nombre.startsWith("modificar") || nombre.startsWith("agregar")
                || nombre.equals("buscarTurnos") || nombre.equals("atenderTurno");
        boolean peligro = nombre.startsWith("eliminar") || nombre.equals("cancelarTurno");
        EstiloClinica.boton(boton, principal, peligro);
        return boton;
    }

    protected JPanel formulario(String[] etiquetas, JTextField[] campos) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 18));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(EstiloClinica.BORDE),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));
        for (int i = 0; i < etiquetas.length; i++) {
            JPanel grupo = new JPanel(new BorderLayout(0, 7));
            JLabel etiqueta = new JLabel(etiquetas[i]);
            etiqueta.setLabelFor(campos[i]);
            grupo.add(etiqueta, BorderLayout.NORTH);
            grupo.add(campos[i], BorderLayout.CENTER);
            panel.add(grupo);
        }
        return panel;
    }

    protected JPanel acciones(Component... componentes) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setOpaque(false);
        for (Component componente : componentes) { panel.add(componente); }
        return panel;
    }

    protected DefaultTableModel modelo(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    protected JTable tabla(DefaultTableModel modelo, String nombre) {
        JTable tabla = new JTable(modelo) {
            public String getToolTipText(java.awt.event.MouseEvent evento) {
                int fila = rowAtPoint(evento.getPoint());
                int columna = columnAtPoint(evento.getPoint());
                if (fila < 0 || columna < 0) { return null; }
                Object valor = getValueAt(fila, columna);
                return valor == null ? null : valor.toString();
            }
        };
        tabla.setName(nombre);
        tabla.setRowHeight(40);
        tabla.setDefaultRenderer(Object.class, EstiloClinica.celdas());
        tabla.setShowVerticalLines(false); tabla.setGridColor(EstiloClinica.BORDE);
        tabla.setSelectionBackground(EstiloClinica.SUAVE);
        tabla.setSelectionForeground(EstiloClinica.TEXTO);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(tabla.getFont().deriveFont(Font.BOLD, 13f));
        tabla.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        return tabla;
    }

    protected void informar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Clinica - informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void error(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Revisar la operacion", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirmar(String mensaje) {
        return JOptionPane.showOptionDialog(this, mensaje, "Confirmar operacion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Confirmar", "Volver"}, "Volver") == JOptionPane.YES_OPTION;
    }

    public abstract void actualizar();
}
