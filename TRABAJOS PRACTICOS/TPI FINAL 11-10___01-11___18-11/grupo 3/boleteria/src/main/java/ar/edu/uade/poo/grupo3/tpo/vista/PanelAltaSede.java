package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.SedeController;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Alta de sede: datos generales + sectores. Al guardar se persiste en datos/sedes.txt y datos/sectores.txt. */
public class PanelAltaSede extends JPanel implements Pantalla {

    private final SedeController controller;
    private final JTextField nombre = new JTextField(20);
    private final JTextField direccion = new JTextField(20);
    private final FormularioSector formulario = new FormularioSector();
    private final DefaultTableModel modeloSectores = FormularioSector.modeloSectores();
    private final JTable tablaSectores = FormularioSector.tablaSectores(modeloSectores);
    private final List<Sector> sectores = new ArrayList<>();
    private final Aviso aviso = new Aviso();

    public PanelAltaSede(SedeController controller) {
        super(new BorderLayout());
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Alta de sede",
                "Cargá los datos de la sede y armá sus sectores. Necesita al menos un sector para guardarse."),
                BorderLayout.NORTH);

        JPanel datos = Estilo.columna(Estilo.seccion("1. Datos de la sede"),
                new JLabel("Nombre"), nombre, new JLabel("Dirección"), direccion);
        JPanel tarjetaDatos = Estilo.tarjeta(datos);
        tarjetaDatos.setPreferredSize(new Dimension(300, 0));

        JButton agregar = new JButton("Agregar sector");
        agregar.addActionListener(e -> agregarSector());
        JButton quitar = new JButton("Quitar seleccionado");
        quitar.addActionListener(e -> quitarSector());
        JPanel botonesSector = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botonesSector.add(agregar);
        botonesSector.add(quitar);

        JPanel arriba = new JPanel(new BorderLayout(0, 10));
        arriba.add(Estilo.seccion("2. Sectores"), BorderLayout.NORTH);
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botonesSector, BorderLayout.SOUTH);
        JPanel sectoresPanel = new JPanel(new BorderLayout(0, 12));
        sectoresPanel.add(arriba, BorderLayout.NORTH);
        sectoresPanel.add(Estilo.scroll(tablaSectores), BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(16, 0));
        centro.add(tarjetaDatos, BorderLayout.WEST);
        centro.add(Estilo.tarjeta(sectoresPanel), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JButton limpiar = new JButton("Limpiar");
        limpiar.addActionListener(e -> limpiarTodo());
        JButton guardar = new JButton("Guardar sede");
        guardar.addActionListener(e -> guardar());
        add(Estilo.barraInferior(aviso, limpiar, guardar), BorderLayout.SOUTH);
    }

    private void agregarSector() {
        try {
            Sector sector = formulario.crearSector();
            boolean repetido = sectores.stream().anyMatch(s -> s.getCodigo().equals(sector.getCodigo()));
            if (repetido) {
                throw new IllegalArgumentException("Ya agregaste un sector con código " + sector.getCodigo());
            }
            sectores.add(sector);
            FormularioSector.llenar(modeloSectores, sectores);
            formulario.limpiar();
            aviso.info("Sector " + sector.getCodigo() + " agregado (" + Estilo.numero(sector.getCapacidad())
                    + " lugares). Falta guardar la sede.");
        } catch (IllegalArgumentException ex) {
            aviso.error(ex.getMessage());
        }
    }

    private void quitarSector() {
        int fila = tablaSectores.getSelectedRow();
        if (fila < 0) {
            aviso.error("Seleccioná el sector que querés quitar.");
            return;
        }
        sectores.remove(fila);
        FormularioSector.llenar(modeloSectores, sectores);
        aviso.ocultar();
    }

    private void guardar() {
        try {
            Sede sede = controller.alta(nombre.getText(), direccion.getText(), sectores);
            limpiarTodo();
            aviso.exito("Sede " + sede.getNombre() + " guardada con " + sede.getSectores().size()
                    + " sectores y " + Estilo.numero(sede.getCapacidadTotal()) + " lugares.");
        } catch (IllegalArgumentException ex) {
            aviso.error(ex.getMessage());
        } catch (IOException ex) {
            aviso.error("No se pudo guardar en el archivo: " + ex.getMessage());
        }
    }

    private void limpiarTodo() {
        nombre.setText("");
        direccion.setText("");
        sectores.clear();
        modeloSectores.setRowCount(0);
        formulario.limpiar();
        aviso.ocultar();
    }

    @Override
    public void alMostrar() {
        nombre.requestFocusInWindow();
    }
}
