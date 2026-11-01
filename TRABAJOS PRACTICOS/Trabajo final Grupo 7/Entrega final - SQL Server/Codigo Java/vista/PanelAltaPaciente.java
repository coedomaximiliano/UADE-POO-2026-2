package vista;

import aplicacion.AplicacionClinica;

import modelo.Medico;
import modelo.Paciente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PanelAltaPaciente extends PanelPantalla {
    private JTextField nombre = campo("nombrePaciente");
    private JTextField dni = campo("dniPaciente");
    private JTextField email = campo("emailPaciente");
    private JTextField historia = campo("historiaPaciente");
    private JComboBox<Object> cabecera = new JComboBox<Object>();
    private Runnable alGuardar;

    public PanelAltaPaciente(AplicacionClinica app) {
        this(app, null);
    }

    public PanelAltaPaciente(AplicacionClinica app, Runnable alGuardar) {
        super(
                app,
                "Registrar paciente",
                "Completá los datos de la persona. * Campos obligatorios. El correo es opcional.");
        this.alGuardar = alGuardar;
        cabecera.setName("cabeceraAltaPaciente");
        EstiloClinica.combo(cabecera);
        cabecera.addItem("Sin médico de cabecera");
        for (Medico m : app.getMedicos().listar()) cabecera.addItem(m);
        JPanel contenido = new JPanel(new BorderLayout(12, 18));
        JPanel datos = new JPanel(new BorderLayout(10, 12));
        datos.add(
                formulario(
                        new String[] {"Nombre *", "DNI *", "Correo", "Historia clinica *"},
                        new JTextField[] {nombre, dni, email, historia}),
                BorderLayout.CENTER);
        JPanel elegir = new JPanel(new BorderLayout(8, 5));
        elegir.add(new JLabel("Médico de cabecera"), BorderLayout.NORTH);
        elegir.add(cabecera, BorderLayout.CENTER);
        datos.add(elegir, BorderLayout.SOUTH);
        contenido.add(datos, BorderLayout.NORTH);
        contenido.add(
                acciones(
                        boton(
                                "Registrar paciente",
                                "guardarPaciente",
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        guardar();
                                    }
                                }),
                        boton(
                                "Limpiar",
                                "limpiarPaciente",
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        limpiar();
                                    }
                                })),
                BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
    }

    private void guardar() {
        try {
            app.getPacientes()
                    .agregar(
                            new Paciente(
                                    nombre.getText(),
                                    dni.getText(),
                                    email.getText(),
                                    historia.getText(),
                                    cabecera.getSelectedItem() instanceof Medico
                                            ? ((Medico) cabecera.getSelectedItem()).getMatricula()
                                            : null));
            informar("Paciente registrado correctamente.");
            limpiar();
            if (alGuardar != null) {
                alGuardar.run();
            }
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    private void limpiar() {
        nombre.setText("");
        dni.setText("");
        email.setText("");
        historia.setText("");
        cabecera.setSelectedIndex(0);
        nombre.requestFocusInWindow();
    }

    public void actualizar() {}
}
