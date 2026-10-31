package vista;

import controlador.TurnoControlador;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Turno;
import modelo.TurnoOcupadoException;

// Capa de presentacion: solo muestra datos y delega en el controlador
public class VentanaTurnos extends JFrame {
    private final TurnoControlador ctrl;
    private final DefaultTableModel tabla =
        new DefaultTableModel(new String[]{"Fecha", "Hora", "Paciente", "Medico"}, 0);
    private final JTable jtable = new JTable(tabla);
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtHora = new JTextField();
    private final JTextField txtPaciente = new JTextField();
    private final JTextField txtMedico = new JTextField();

    public VentanaTurnos(TurnoControlador ctrl) {
        super("Consultorio - Turnos");
        this.ctrl = ctrl;
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(crearMenu());

        JPanel form = new JPanel(new GridLayout(2, 4, 5, 5));
        form.add(new JLabel("Fecha (dd/mm)"));  form.add(new JLabel("Hora (hh:mm)"));
        form.add(new JLabel("Paciente"));       form.add(new JLabel("Medico"));
        form.add(txtFecha); form.add(txtHora); form.add(txtPaciente); form.add(txtMedico);

        JButton btnReservar = new JButton("Reservar");
        JButton btnCancelar = new JButton("Cancelar seleccionado");
        JPanel botones = new JPanel();
        botones.add(btnReservar);
        botones.add(btnCancelar);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(jtable), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnReservar.addActionListener(e -> reservar());
        btnCancelar.addActionListener(e -> cancelar());
        refrescar();
    }

    private JMenuBar crearMenu() {
        JMenu archivo = new JMenu("Archivo");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem salir = new JMenuItem("Salir");
        guardar.addActionListener(e -> guardar());
        salir.addActionListener(e -> dispose());
        archivo.add(guardar);
        archivo.addSeparator();
        archivo.add(salir);

        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem acerca = new JMenuItem("Acerca de");
        acerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistema de turnos - Integrador Clase 14"));
        ayuda.add(acerca);

        JMenuBar barra = new JMenuBar();
        barra.add(archivo);
        barra.add(ayuda);
        return barra;
    }

    private void reservar() {
        if (txtPaciente.getText().isBlank() || txtMedico.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Complete paciente y medico",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ctrl.reservar(new Turno(txtFecha.getText(), txtHora.getText(),
                                    txtPaciente.getText(), txtMedico.getText()));
            refrescar();
        } catch (TurnoOcupadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Turno ocupado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        int fila = jtable.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno de la tabla");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Cancelar el turno seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            Turno t = ctrl.listarOrdenados().get(fila);   // la tabla muestra la lista ordenada
            ctrl.cancelar(t);
            refrescar();
        }
    }

    private void guardar() {
        try {
            ctrl.guardar();
            JOptionPane.showMessageDialog(this, "Turnos guardados");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescar() {
        tabla.setRowCount(0);
        for (Turno t : ctrl.listarOrdenados()) {
            tabla.addRow(new Object[]{t.getFecha(), t.getHora(), t.getPaciente(), t.getMedico()});
        }
    }
}
