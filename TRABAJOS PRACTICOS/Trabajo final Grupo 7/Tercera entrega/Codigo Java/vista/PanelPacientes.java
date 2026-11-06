package vista;

import aplicacion.AplicacionClinica;
import modelo.Paciente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelPacientes extends PanelPantalla {
    private DefaultTableModel modelo = modelo(new String[]{"DNI", "Nombre", "Correo", "Historia clinica"});
    private JTable tabla = tabla(modelo, "tablaPacientes");
    private JTextField nombre = campo("editarNombrePaciente");
    private JTextField email = campo("editarEmailPaciente");
    private JTextField historia = campo("editarHistoriaPaciente");
    private JButton btnGuardar;
    private JButton btnEliminar;

    public PanelPacientes(AplicacionClinica app) {
        super(app, "Pacientes", "Seleccioná una fila para editar sus datos o usá + para agregar una persona.");
        JButton agregar = boton("+", "agregarPaciente", new ActionListener() {
            public void actionPerformed(ActionEvent e) { abrirAlta(); }
        });
        agregar.setFont(agregar.getFont().deriveFont(26f));
        agregar.setToolTipText("Agregar paciente");
        agregar.getAccessibleContext().setAccessibleName("Agregar paciente");
        cabecera.add(agregar, BorderLayout.EAST);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JPanel edicion = new JPanel(new BorderLayout(8, 8));
        edicion.add(formulario(new String[]{"Nombre", "Correo", "Historia clinica"},
                new JTextField[]{nombre, email, historia}), BorderLayout.CENTER);
        btnGuardar = boton("Guardar cambios", "modificarPaciente", new ActionListener() {
            public void actionPerformed(ActionEvent e) { modificar(); }
        });
        btnEliminar = boton("Eliminar paciente", "eliminarPaciente", new ActionListener() {
            public void actionPerformed(ActionEvent e) { eliminar(); }
        });
        edicion.add(acciones(btnGuardar, btnEliminar), BorderLayout.SOUTH);
        add(edicion, BorderLayout.SOUTH);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) { seleccionar(); }
        });
        actualizar();
    }

    private void abrirAlta() {
        final JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this), "Agregar paciente",
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setName("altaPacienteDialogo");
        dialogo.setContentPane(new PanelAltaPaciente(app, new Runnable() {
            public void run() { actualizar(); dialogo.dispose(); }
        }));
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(800, 440); dialogo.setMinimumSize(new java.awt.Dimension(760, 420));
        dialogo.setLocationRelativeTo(this); dialogo.setVisible(true);
    }

    private String dniSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { throw new IllegalArgumentException("Seleccionar un paciente de la tabla."); }
        return modelo.getValueAt(fila, 0).toString();
    }

    private void seleccionar() {
        int fila = tabla.getSelectedRow();
        boolean seleccionado = fila != -1;
        btnGuardar.setEnabled(seleccionado); btnEliminar.setEnabled(seleccionado);
        nombre.setEnabled(seleccionado); email.setEnabled(seleccionado); historia.setEnabled(seleccionado);
        if (fila != -1) {
            nombre.setText(modelo.getValueAt(fila, 1).toString());
            email.setText(modelo.getValueAt(fila, 2).toString());
            historia.setText(modelo.getValueAt(fila, 3).toString());
        }
    }

    private void modificar() {
        try {
            app.getPacientes().modificar(new Paciente(nombre.getText(), dniSeleccionado(), email.getText(), historia.getText()));
            actualizar(); informar("Datos del paciente actualizados.");
        } catch (IllegalArgumentException e) { error(e); }
    }

    private void eliminar() {
        try {
            String dni = dniSeleccionado();
            Paciente paciente = app.getPacientes().buscarPorDni(dni);
            if (confirmar("¿Eliminar a " + paciente.getNombre() + "?\nDNI: " + dni
                    + "\n\nSolo se puede eliminar si no tiene historial de turnos.")) {
                app.eliminarPaciente(dni); actualizar(); informar("Paciente eliminado.");
            }
        } catch (IllegalArgumentException e) { error(e); }
    }

    public void actualizar() {
        tabla.clearSelection(); modelo.setRowCount(0);
        for (Paciente paciente : app.getPacientes().listar()) {
            modelo.addRow(new Object[]{paciente.getDni(), paciente.getNombre(), paciente.getEmail(), paciente.getNumeroHistoriaClinica()});
        }
        nombre.setText(""); email.setText(""); historia.setText("");
        seleccionar();
    }
}
