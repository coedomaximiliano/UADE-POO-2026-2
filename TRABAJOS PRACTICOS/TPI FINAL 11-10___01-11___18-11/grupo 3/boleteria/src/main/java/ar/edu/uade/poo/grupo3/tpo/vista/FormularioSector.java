package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.modelo.enums.TipoSector;
import ar.edu.uade.poo.grupo3.tpo.modelo.infraestructura.Sector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.util.List;

/**
 * Formulario para cargar un sector. Se reutiliza en el alta y en la modificación de sedes.
 * Según el tipo elegido habilita filas y asientos por fila (numerado) o capacidad (general).
 */
public class FormularioSector extends JPanel {

    private final JTextField codigo = new JTextField(10);
    private final JTextField nombre = new JTextField(10);
    private final JComboBox<TipoSector> tipo = new JComboBox<>(TipoSector.values());
    private final JSpinner piso = spinner(0, 0, 10, 1);
    private final JSpinner distancia = spinner(10, 0, 500, 1);
    private final JSpinner filas = spinner(5, 1, 26, 1);
    private final JSpinner asientosPorFila = spinner(20, 1, 100, 1);
    private final JSpinner capacidad = spinner(500, 1, 100000, 50);
    private final JLabel lblFilas = new JLabel("Filas (A, B, C...)");
    private final JLabel lblAsientos = new JLabel("Asientos por fila");
    private final JLabel lblCapacidad = new JLabel("Capacidad");

    public FormularioSector() {
        super(new GridLayout(3, 6, 10, 6));
        codigo.setToolTipText("Código corto, por ejemplo FLR, 100 o CAMPO");

        add(new JLabel("Código"));
        add(codigo);
        add(new JLabel("Piso"));
        add(piso);
        add(lblFilas);
        add(filas);

        add(new JLabel("Nombre"));
        add(nombre);
        add(new JLabel("Distancia (m)"));
        add(distancia);
        add(lblAsientos);
        add(asientosPorFila);

        add(new JLabel("Tipo"));
        add(tipo);
        add(new JLabel());
        add(new JLabel());
        add(lblCapacidad);
        add(capacidad);

        tipo.addActionListener(e -> actualizarCampos());
        actualizarCampos();
    }

    private void actualizarCampos() {
        boolean numerado = tipo.getSelectedItem() == TipoSector.NUMERADO;
        filas.setEnabled(numerado);
        asientosPorFila.setEnabled(numerado);
        capacidad.setEnabled(!numerado);
        lblFilas.setEnabled(numerado);
        lblAsientos.setEnabled(numerado);
        lblCapacidad.setEnabled(!numerado);
    }

    /** Crea el sector con la fábrica. Si un dato es inválido lanza IllegalArgumentException. */
    public Sector crearSector() {
        return Sector.crear((TipoSector) tipo.getSelectedItem(), codigo.getText(), nombre.getText(),
                (Integer) piso.getValue(), ((Integer) distancia.getValue()).doubleValue(),
                (Integer) filas.getValue(), (Integer) asientosPorFila.getValue(), (Integer) capacidad.getValue());
    }

    public void limpiar() {
        codigo.setText("");
        nombre.setText("");
        codigo.requestFocusInWindow();
    }

    @Override
    public void setEnabled(boolean habilitado) {
        super.setEnabled(habilitado);
        for (JComponent c : List.of(codigo, nombre, tipo, piso, distancia)) {
            c.setEnabled(habilitado);
        }
        if (habilitado) {
            actualizarCampos();
        } else {
            filas.setEnabled(false);
            asientosPorFila.setEnabled(false);
            capacidad.setEnabled(false);
        }
    }

    private static JSpinner spinner(int valor, int minimo, int maximo, int paso) {
        return new JSpinner(new SpinnerNumberModel(valor, minimo, maximo, paso));
    }

    // ------------------------------------------------------------------ tabla de sectores compartida

    public static DefaultTableModel modeloSectores() {
        return Estilo.modeloTabla("Código", "Nombre", "Tipo", "Piso", "Distancia (m)", "Capacidad", "Precio x");
    }

    public static JTable tablaSectores(DefaultTableModel modelo) {
        JTable tabla = Estilo.tabla(modelo);
        Estilo.anchos(tabla, 70, 150, 130, 50, 100, 90, 80);
        return tabla;
    }

    public static void llenar(DefaultTableModel modelo, List<Sector> sectores) {
        modelo.setRowCount(0);
        for (Sector s : sectores) {
            modelo.addRow(new Object[]{s.getCodigo(), s.getNombre(), s.getTipo().toString(), s.getPiso(),
                    (int) s.getDistanciaEscenario(), s.getCapacidad(),
                    String.format("x %.2f", s.calcularMultiplicador())});
        }
    }
}
