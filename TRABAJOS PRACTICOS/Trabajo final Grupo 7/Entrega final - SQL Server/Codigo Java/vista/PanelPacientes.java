package vista;

import aplicacion.AplicacionClinica;

import modelo.Medico;
import modelo.Paciente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelPacientes extends PanelPantalla {
    private DefaultTableModel modelo =
            modelo(
                    new String[] {
                        "DNI", "Nombre", "Correo", "Historia clinica", "Médico de cabecera"
                    });
    private JTable tabla = tabla(modelo, "tablaPacientes");
    private JTextField nombre = campo("editarNombrePaciente");
    private JTextField email = campo("editarEmailPaciente");
    private JTextField historia = campo("editarHistoriaPaciente");
    private JComboBox<Object> cabecera = new JComboBox<Object>();
    private java.util.HashMap<String, Paciente> porDni = new java.util.HashMap<String, Paciente>();
    private JButton btnGuardar;
    private JButton btnEliminar;

    public PanelPacientes(AplicacionClinica app) {
        super(
                app,
                "Pacientes",
                "Seleccioná una fila para editar sus datos o usá + para agregar una persona.");
        JButton agregar =
                boton(
                        "+",
                        "agregarPaciente",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                abrirAlta();
                            }
                        });
        agregar.setFont(agregar.getFont().deriveFont(26f));
        agregar.setToolTipText("Agregar paciente");
        agregar.getAccessibleContext().setAccessibleName("Agregar paciente");
        cabecera.add(agregar, BorderLayout.EAST);
        JScrollPane listado = new JScrollPane(tabla);
        listado.setMinimumSize(new java.awt.Dimension(300, 130));
        JPanel edicion = new JPanel(new BorderLayout(8, 8));
        JPanel datos = new JPanel(new BorderLayout(8, 8));
        datos.add(
                formulario(
                        new String[] {"Nombre", "Correo", "Historia clinica"},
                        new JTextField[] {nombre, email, historia}),
                BorderLayout.CENTER);
        JPanel elegir = new JPanel(new BorderLayout(8, 5));
        elegir.add(new JLabel("Médico de cabecera"), BorderLayout.NORTH);
        cabecera.setName("cabeceraEditarPaciente");
        EstiloClinica.combo(cabecera);
        elegir.add(cabecera, BorderLayout.CENTER);
        datos.add(elegir, BorderLayout.SOUTH);
        edicion.add(datos, BorderLayout.CENTER);
        btnGuardar =
                boton(
                        "Guardar cambios",
                        "modificarPaciente",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                modificar();
                            }
                        });
        btnEliminar =
                boton(
                        "Eliminar paciente",
                        "eliminarPaciente",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                eliminar();
                            }
                        });
        edicion.add(acciones(btnGuardar, btnEliminar), BorderLayout.SOUTH);
        edicion.setMinimumSize(new java.awt.Dimension(300, 290));
        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listado, edicion);
        division.setResizeWeight(0.55);
        division.setDividerLocation(145);
        add(division, BorderLayout.CENTER);
        tabla.getSelectionModel()
                .addListSelectionListener(
                        new ListSelectionListener() {
                            public void valueChanged(ListSelectionEvent e) {
                                seleccionar();
                            }
                        });
        actualizar();
    }

    private void abrirAlta() {
        final JDialog dialogo =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Agregar paciente",
                        java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setName("altaPacienteDialogo");
        dialogo.setContentPane(
                new PanelAltaPaciente(
                        app,
                        new Runnable() {
                            public void run() {
                                actualizar();
                                dialogo.dispose();
                            }
                        }));
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(900, 560);
        dialogo.setMinimumSize(new java.awt.Dimension(850, 530));
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private String dniSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            throw new IllegalArgumentException("Seleccionar un paciente de la tabla.");
        }
        return modelo.getValueAt(fila, 0).toString();
    }

    private void seleccionar() {
        int fila = tabla.getSelectedRow();
        boolean seleccionado = fila != -1;
        btnGuardar.setEnabled(seleccionado);
        btnEliminar.setEnabled(seleccionado);
        nombre.setEnabled(seleccionado);
        email.setEnabled(seleccionado);
        historia.setEnabled(seleccionado);
        cabecera.setEnabled(seleccionado);
        if (fila != -1) {
            Paciente datos = porDni.get(modelo.getValueAt(fila, 0).toString());
            cabecera.setSelectedIndex(0);
            for (int i = 1; i < cabecera.getItemCount(); i++)
                if (((Medico) cabecera.getItemAt(i))
                        .getMatricula()
                        .equals(datos.getMedicoCabecera())) cabecera.setSelectedIndex(i);
            nombre.setText(modelo.getValueAt(fila, 1).toString());
            email.setText(modelo.getValueAt(fila, 2).toString());
            historia.setText(modelo.getValueAt(fila, 3).toString());
        }
    }

    private void modificar() {
        try {
            app.getPacientes()
                    .modificar(
                            new Paciente(
                                    nombre.getText(),
                                    dniSeleccionado(),
                                    email.getText(),
                                    historia.getText(),
                                    cabecera.getSelectedItem() instanceof Medico
                                            ? ((Medico) cabecera.getSelectedItem()).getMatricula()
                                            : null));
            actualizar();
            informar("Datos del paciente actualizados.");
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    private void eliminar() {
        try {
            String dni = dniSeleccionado();
            Paciente paciente = app.getPacientes().buscarPorDni(dni);
            if (confirmar(
                    "¿Eliminar a "
                            + paciente.getNombre()
                            + "?\nDNI: "
                            + dni
                            + "\n\nSolo se puede eliminar si no tiene historial de turnos.")) {
                app.eliminarPaciente(dni);
                actualizar();
                informar("Paciente eliminado.");
            }
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    public void actualizar() {
        tabla.clearSelection();
        modelo.setRowCount(0);
        porDni.clear();
        cabecera.removeAllItems();
        cabecera.addItem("Sin médico de cabecera");
        java.util.HashMap<String, String> nombres = new java.util.HashMap<String, String>();
        for (Medico m : app.getMedicos().listar()) {
            cabecera.addItem(m);
            nombres.put(m.getMatricula(), m.getNombre());
        }
        for (Paciente paciente : app.getPacientes().listar()) {
            porDni.put(paciente.getDni(), paciente);
            modelo.addRow(
                    new Object[] {
                        paciente.getDni(),
                        paciente.getNombre(),
                        paciente.getEmail(),
                        paciente.getNumeroHistoriaClinica(),
                        nombres.getOrDefault(paciente.getMedicoCabecera(), "Sin asignar")
                    });
        }
        nombre.setText("");
        email.setText("");
        historia.setText("");
        seleccionar();
    }
}
