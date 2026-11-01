package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.SedeController;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sede;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/** Consulta (la "C" del ABM): lista de sedes con buscador y el detalle de sectores de la elegida. */
public class PanelConsultaSedes extends JPanel implements Pantalla {

    private final SedeController controller;
    private final JTextField buscador = new JTextField(20);
    private final DefaultTableModel modeloSedes =
            Estilo.modeloTabla("Nombre", "Dirección", "Sectores", "Capacidad", "Funciones activas");
    private final JTable tablaSedes = Estilo.tabla(modeloSedes);
    private final DefaultTableModel modeloSectores = FormularioSector.modeloSectores();
    private final JLabel tituloSectores = Estilo.seccion("Sectores");
    private final List<Sede> visibles = new ArrayList<>();

    public PanelConsultaSedes(SedeController controller) {
        super(new BorderLayout());
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Consulta de sedes",
                "Buscá una sede y mirá sus sectores, capacidad y cuánto multiplica el precio cada ubicación."),
                BorderLayout.NORTH);

        Estilo.anchos(tablaSedes, 200, 300, 80, 100, 130);
        buscador.setToolTipText("Buscar por nombre o dirección");
        buscador.addActionListener(e -> alMostrar());
        JButton buscar = new JButton("Buscar");
        buscar.addActionListener(e -> alMostrar());
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        busqueda.add(new JLabel("Buscar"));
        busqueda.add(buscador);
        busqueda.add(buscar);
        JPanel barra = new JPanel(new BorderLayout());
        barra.add(Estilo.seccion("Sedes"), BorderLayout.WEST);
        barra.add(busqueda, BorderLayout.EAST);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel listado = new JPanel(new BorderLayout());
        listado.add(barra, BorderLayout.NORTH);
        listado.add(Estilo.scroll(tablaSedes), BorderLayout.CENTER);

        JPanel detalle = new JPanel(new BorderLayout(0, 10));
        detalle.add(tituloSectores, BorderLayout.NORTH);
        detalle.add(Estilo.scroll(FormularioSector.tablaSectores(modeloSectores)), BorderLayout.CENTER);

        JPanel centro = new JPanel(new GridLayout(2, 1, 0, 16));
        centro.add(Estilo.tarjeta(listado));
        centro.add(Estilo.tarjeta(detalle));
        add(centro, BorderLayout.CENTER);

        tablaSedes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarSectores();
            }
        });
    }

    @Override
    public void alMostrar() {
        visibles.clear();
        visibles.addAll(controller.listar(buscador.getText()));
        modeloSedes.setRowCount(0);
        for (Sede s : visibles) {
            modeloSedes.addRow(new Object[]{s.getNombre(), s.getDireccion(), s.getSectores().size(),
                    Estilo.numero(s.getCapacidadTotal()), controller.funcionesActivas(s)});
        }
        if (!visibles.isEmpty()) {
            tablaSedes.setRowSelectionInterval(0, 0);
        } else {
            modeloSectores.setRowCount(0);
            tituloSectores.setText("Sectores");
        }
    }

    private void mostrarSectores() {
        int fila = tablaSedes.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Sede sede = visibles.get(fila);
        tituloSectores.setText("Sectores de " + sede.getNombre());
        FormularioSector.llenar(modeloSectores, sede.getSectores());
    }
}
