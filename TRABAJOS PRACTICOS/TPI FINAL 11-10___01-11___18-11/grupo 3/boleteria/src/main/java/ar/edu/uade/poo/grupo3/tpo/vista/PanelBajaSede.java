package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.SedeController;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Baja de sede: se pide confirmación y no se permite si la sede tiene funciones programadas. */
public class PanelBajaSede extends JPanel implements Pantalla {

    private final SedeController controller;
    private final DefaultTableModel modelo =
            Estilo.modeloTabla("Nombre", "Dirección", "Sectores", "Capacidad", "Funciones activas");
    private final JTable tabla = Estilo.tabla(modelo);
    private final JLabel detalle = Estilo.subtitulo("Seleccioná una sede de la lista.");
    private final Aviso aviso = new Aviso();
    private final List<Sede> sedes = new ArrayList<>();

    public PanelBajaSede(SedeController controller) {
        super(new BorderLayout());
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Baja de sede",
                "Elegí la sede que querés dar de baja. Las sedes con funciones programadas no se pueden eliminar."),
                BorderLayout.NORTH);

        Estilo.anchos(tabla, 200, 300, 80, 100, 130);
        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.add(Estilo.seccion("Sedes registradas"), BorderLayout.NORTH);
        contenido.add(Estilo.scroll(tabla), BorderLayout.CENTER);
        contenido.add(detalle, BorderLayout.SOUTH);
        add(Estilo.tarjeta(contenido), BorderLayout.CENTER);

        JButton eliminar = new JButton("Dar de baja");
        eliminar.addActionListener(e -> darDeBaja());
        add(Estilo.barraInferior(aviso, eliminar), BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            Sede sede = seleccionada();
            if (sede != null) {
                int funciones = controller.funcionesActivas(sede);
                detalle.setText(sede.getNombre() + " · " + sede.getSectores().size() + " sectores · "
                        + Estilo.numero(sede.getCapacidadTotal()) + " lugares"
                        + (funciones > 0 ? " · tiene " + funciones + " función(es) programada(s)" : ""));
            }
        });
    }

    private Sede seleccionada() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : sedes.get(fila);
    }

    @Override
    public void alMostrar() {
        sedes.clear();
        sedes.addAll(controller.listar(""));
        modelo.setRowCount(0);
        for (Sede s : sedes) {
            modelo.addRow(new Object[]{s.getNombre(), s.getDireccion(), s.getSectores().size(),
                    Estilo.numero(s.getCapacidadTotal()), controller.funcionesActivas(s)});
        }
        detalle.setText("Seleccioná una sede de la lista.");
    }

    private void darDeBaja() {
        Sede sede = seleccionada();
        if (sede == null) {
            aviso.error("Seleccioná la sede que querés dar de baja.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que querés dar de baja " + sede.getNombre() + " y sus "
                        + sede.getSectores().size() + " sectores?",
                "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            controller.baja(sede);
            alMostrar();
            aviso.exito("Sede " + sede.getNombre() + " dada de baja.");
        } catch (IllegalStateException ex) {
            aviso.error(ex.getMessage());
        } catch (IOException ex) {
            aviso.error("No se pudo guardar en el archivo: " + ex.getMessage());
        }
    }
}
