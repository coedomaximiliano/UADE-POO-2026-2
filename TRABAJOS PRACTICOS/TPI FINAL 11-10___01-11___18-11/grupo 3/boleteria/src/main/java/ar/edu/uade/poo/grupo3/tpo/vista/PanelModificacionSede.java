package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.SedeController;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.List;

/**
 * Modificación de sede: cambiar nombre y dirección, y agregar o quitar sectores.
 * Si la sede tiene funciones programadas, sus sectores quedan bloqueados (ya hay entradas en juego).
 */
public class PanelModificacionSede extends JPanel implements Pantalla {

    private final SedeController controller;
    private final JComboBox<Sede> selector = new JComboBox<>();
    private final JTextField nombre = new JTextField(20);
    private final JTextField direccion = new JTextField(20);
    private final FormularioSector formulario = new FormularioSector();
    private final DefaultTableModel modeloSectores = FormularioSector.modeloSectores();
    private final JTable tablaSectores = FormularioSector.tablaSectores(modeloSectores);
    private final JButton agregar = new JButton("Agregar sector");
    private final JButton quitar = new JButton("Quitar seleccionado");
    private final Aviso bloqueo = new Aviso();
    private final Aviso aviso = new Aviso();
    private boolean cargando;

    public PanelModificacionSede(SedeController controller) {
        super(new BorderLayout());
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(Estilo.encabezado("Modificación de sede",
                "Elegí una sede para cambiar sus datos o sus sectores. Los cambios se guardan al instante."),
                BorderLayout.NORTH);
        JPanel eleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        eleccion.add(new JLabel("Sede a modificar"));
        selector.setPreferredSize(new Dimension(320, 26));
        eleccion.add(selector);
        eleccion.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        norte.add(eleccion, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        JButton guardarDatos = new JButton("Guardar datos");
        guardarDatos.addActionListener(e -> guardarDatos());
        JPanel datos = Estilo.columna(Estilo.seccion("Datos de la sede"),
                new JLabel("Nombre"), nombre, new JLabel("Dirección"), direccion, guardarDatos);
        JPanel tarjetaDatos = Estilo.tarjeta(datos);
        tarjetaDatos.setPreferredSize(new Dimension(300, 0));

        agregar.addActionListener(e -> agregarSector());
        quitar.addActionListener(e -> quitarSector());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.add(agregar);
        botones.add(quitar);

        JPanel tituloYBloqueo = new JPanel(new BorderLayout(0, 8));
        tituloYBloqueo.add(Estilo.seccion("Sectores"), BorderLayout.NORTH);
        tituloYBloqueo.add(bloqueo, BorderLayout.CENTER);
        JPanel arriba = new JPanel(new BorderLayout(0, 10));
        arriba.add(tituloYBloqueo, BorderLayout.NORTH);
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);
        JPanel sectores = new JPanel(new BorderLayout(0, 12));
        sectores.add(arriba, BorderLayout.NORTH);
        sectores.add(Estilo.scroll(tablaSectores), BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(16, 0));
        centro.add(tarjetaDatos, BorderLayout.WEST);
        centro.add(Estilo.tarjeta(sectores), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        add(Estilo.barraInferior(aviso), BorderLayout.SOUTH);

        selector.addActionListener(e -> {
            if (!cargando) {
                aviso.ocultar();
                mostrarSede();
            }
        });
    }

    private Sede seleccionada() {
        return (Sede) selector.getSelectedItem();
    }

    @Override
    public void alMostrar() {
        Sede anterior = seleccionada();
        cargando = true;
        selector.removeAllItems();
        controller.listar("").forEach(selector::addItem);
        if (anterior != null && controller.listar("").contains(anterior)) {
            selector.setSelectedItem(anterior);
        }
        cargando = false;
        aviso.ocultar();
        mostrarSede();
    }

    private void mostrarSede() {
        Sede sede = seleccionada();
        boolean haySede = sede != null;
        nombre.setText(haySede ? sede.getNombre() : "");
        direccion.setText(haySede ? sede.getDireccion() : "");
        FormularioSector.llenar(modeloSectores, haySede ? sede.getSectores() : List.of());
        int funciones = haySede ? controller.funcionesActivas(sede) : 0;
        boolean editable = haySede && funciones == 0;
        formulario.setEnabled(editable);
        agregar.setEnabled(editable);
        quitar.setEnabled(editable);
        if (haySede && funciones > 0) {
            bloqueo.info("Tiene " + funciones + " función(es) programada(s): los sectores no se pueden cambiar.");
        } else {
            bloqueo.ocultar();
        }
    }

    private void guardarDatos() {
        Sede sede = seleccionada();
        if (sede == null) {
            aviso.error("No hay ninguna sede para modificar.");
            return;
        }
        try {
            controller.modificarDatos(sede, nombre.getText(), direccion.getText());
            selector.repaint();
            aviso.exito("Datos de " + sede.getNombre() + " actualizados.");
        } catch (IllegalArgumentException ex) {
            aviso.error(ex.getMessage());
        } catch (IOException ex) {
            aviso.error("No se pudo guardar en el archivo: " + ex.getMessage());
        }
    }

    private void agregarSector() {
        Sede sede = seleccionada();
        try {
            Sector sector = formulario.crearSector();
            controller.agregarSector(sede, sector);
            formulario.limpiar();
            mostrarSede();
            aviso.exito("Sector " + sector.getCodigo() + " agregado a " + sede.getNombre() + ".");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            aviso.error(ex.getMessage());
        } catch (IOException ex) {
            aviso.error("No se pudo guardar en el archivo: " + ex.getMessage());
        }
    }

    private void quitarSector() {
        Sede sede = seleccionada();
        int fila = tablaSectores.getSelectedRow();
        if (fila < 0) {
            aviso.error("Seleccioná el sector que querés quitar.");
            return;
        }
        String codigo = (String) modeloSectores.getValueAt(fila, 0);
        try {
            controller.quitarSector(sede, codigo);
            mostrarSede();
            aviso.exito("Sector " + codigo + " quitado de " + sede.getNombre() + ".");
        } catch (IllegalStateException ex) {
            aviso.error(ex.getMessage());
        } catch (IOException ex) {
            aviso.error("No se pudo guardar en el archivo: " + ex.getMessage());
        }
    }
}
