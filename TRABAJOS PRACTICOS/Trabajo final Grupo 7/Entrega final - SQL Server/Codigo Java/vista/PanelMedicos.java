package vista;

import aplicacion.AplicacionClinica;

import modelo.Medico;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelMedicos extends PanelPantalla {
    private DefaultTableModel modelo =
            modelo(
                    new String[] {
                        "Matricula",
                        "DNI",
                        "Nombre",
                        "Correo",
                        "Especialidad",
                        "Consultorio",
                        "Días y horarios"
                    });
    private JTable tabla = tabla(modelo, "tablaMedicos");
    private JTextField nombre = campo("editarNombreMedico");
    private JTextField email = campo("editarEmailMedico");
    private JTextField especialidad = campo("editarEspecialidadMedico");
    private JTextField consultorio = campo("editarConsultorioMedico");
    private EditorHorarioMedico horarios = new EditorHorarioMedico();
    private java.util.HashMap<String, Medico> porMatricula =
            new java.util.HashMap<String, Medico>();
    private JButton btnGuardar;
    private JButton btnEliminar;

    public PanelMedicos(AplicacionClinica app) {
        super(
                app,
                "Médicos",
                "Seleccioná una fila para editar sus datos o usá + para agregar un profesional.");
        JButton agregar =
                boton(
                        "+",
                        "agregarMedico",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                abrirAlta();
                            }
                        });
        agregar.setFont(agregar.getFont().deriveFont(26f));
        agregar.setToolTipText("Agregar médico");
        agregar.getAccessibleContext().setAccessibleName("Agregar médico");
        cabecera.add(agregar, BorderLayout.EAST);
        JScrollPane listado = new JScrollPane(tabla);
        listado.setMinimumSize(new java.awt.Dimension(300, 130));
        JPanel edicion = new JPanel(new BorderLayout(8, 8));
        JTabbedPane fichas = new JTabbedPane();
        fichas.addTab(
                "Datos del médico",
                new JScrollPane(
                        formulario(
                                new String[] {"Nombre", "Correo", "Especialidad", "Consultorio"},
                                new JTextField[] {nombre, email, especialidad, consultorio})));
        fichas.addTab("Días y horarios de atención", new JScrollPane(horarios));
        edicion.add(fichas, BorderLayout.CENTER);
        btnGuardar =
                boton(
                        "Guardar cambios",
                        "modificarMedico",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                modificar();
                            }
                        });
        btnEliminar =
                boton(
                        "Eliminar médico",
                        "eliminarMedico",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                eliminar();
                            }
                        });
        JButton ausencias =
                boton(
                        "Ausencias / Vacaciones",
                        "abrirAusencias",
                        e -> {
                            String matricula =
                                    tabla.getSelectedRow() < 0
                                            ? null
                                            : modelo.getValueAt(tabla.getSelectedRow(), 0)
                                                    .toString();
                            PanelAusencias.abrir(this, app, matricula);
                        });
        edicion.add(acciones(btnGuardar, btnEliminar, ausencias), BorderLayout.SOUTH);
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
                        "Agregar médico",
                        java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setName("altaMedicoDialogo");
        dialogo.setContentPane(
                new PanelAltaMedico(
                        app,
                        new Runnable() {
                            public void run() {
                                actualizar();
                                dialogo.dispose();
                            }
                        }));
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(1100, 650);
        dialogo.setMinimumSize(new java.awt.Dimension(1000, 620));
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private int filaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            throw new IllegalArgumentException("Seleccionar un medico de la tabla.");
        }
        return fila;
    }

    private void seleccionar() {
        int fila = tabla.getSelectedRow();
        boolean seleccionado = fila != -1;
        btnGuardar.setEnabled(seleccionado);
        btnEliminar.setEnabled(seleccionado);
        nombre.setEnabled(seleccionado);
        email.setEnabled(seleccionado);
        especialidad.setEnabled(seleccionado);
        consultorio.setEnabled(seleccionado);
        if (fila != -1) {
            Medico datos = porMatricula.get(modelo.getValueAt(fila, 0).toString());
            consultorio.setText(datos.getConsultorio());
            horarios.setHorarios(datos.getHorarios());
            nombre.setText(modelo.getValueAt(fila, 2).toString());
            email.setText(modelo.getValueAt(fila, 3).toString());
            especialidad.setText(modelo.getValueAt(fila, 4).toString());
        }
    }

    private void modificar() {
        try {
            int fila = filaSeleccionada();
            app.getMedicos()
                    .modificar(
                            new Medico(
                                    nombre.getText(),
                                    modelo.getValueAt(fila, 1).toString(),
                                    email.getText(),
                                    modelo.getValueAt(fila, 0).toString(),
                                    especialidad.getText(),
                                    consultorio.getText(),
                                    horarios.getHorarios()));
            actualizar();
            informar("Datos del medico actualizados.");
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    private void eliminar() {
        try {
            String matricula = modelo.getValueAt(filaSeleccionada(), 0).toString();
            Medico medico = app.getMedicos().buscarPorMatricula(matricula);
            if (confirmar(
                    "¿Eliminar a "
                            + medico.getNombre()
                            + "?\nMatrícula: "
                            + matricula
                            + "\n\nSolo se puede eliminar si no tiene turnos ni pacientes de cabecera.")) {
                app.eliminarMedico(matricula);
                actualizar();
                informar("Medico eliminado.");
            }
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    public void actualizar() {
        tabla.clearSelection();
        modelo.setRowCount(0);
        porMatricula.clear();
        for (Medico medico : app.getMedicos().listar()) {
            porMatricula.put(medico.getMatricula(), medico);
            modelo.addRow(
                    new Object[] {
                        medico.getMatricula(),
                        medico.getDni(),
                        medico.getNombre(),
                        medico.getEmail(),
                        medico.getEspecialidad(),
                        medico.getConsultorio(),
                        medico.mostrarHorarios()
                    });
        }
        nombre.setText("");
        email.setText("");
        especialidad.setText("");
        consultorio.setText("");
        horarios.setHorarios(new java.util.ArrayList<modelo.HorarioAtencion>());
        seleccionar();
    }
}
