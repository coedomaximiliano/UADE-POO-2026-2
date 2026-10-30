package vista;

import aplicacion.AplicacionClinica;
import modelo.Medico;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PanelAltaMedico extends PanelPantalla {
    private JTextField nombre = campo("nombreMedico");
    private JTextField dni = campo("dniMedico");
    private JTextField email = campo("emailMedico");
    private JTextField matricula = campo("matriculaMedico");
    private JTextField especialidad = campo("especialidadMedico");
    private Runnable alGuardar;

    public PanelAltaMedico(AplicacionClinica app) {
        this(app, null);
    }

    public PanelAltaMedico(AplicacionClinica app, Runnable alGuardar) {
        super(app, "Registrar médico", "Completá los datos del profesional. * Campos obligatorios. El correo es opcional.");
        this.alGuardar = alGuardar;
        JPanel contenido = new JPanel(new BorderLayout(12, 18));
        contenido.add(formulario(new String[]{"Nombre *", "DNI *", "Correo", "Matricula *", "Especialidad *"},
                new JTextField[]{nombre, dni, email, matricula, especialidad}), BorderLayout.NORTH);
        contenido.add(acciones(boton("Registrar medico", "guardarMedico", new ActionListener() {
            public void actionPerformed(ActionEvent e) { guardar(); }
        }), boton("Limpiar", "limpiarMedico", new ActionListener() {
            public void actionPerformed(ActionEvent e) { limpiar(); }
        })), BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
    }

    private void guardar() {
        try {
            app.getMedicos().agregar(new Medico(nombre.getText(), dni.getText(), email.getText(),
                    matricula.getText(), especialidad.getText()));
            informar("Medico registrado correctamente."); limpiar();
            if (alGuardar != null) { alGuardar.run(); }
        } catch (IllegalArgumentException e) { error(e); }
    }

    private void limpiar() {
        nombre.setText(""); dni.setText(""); email.setText(""); matricula.setText(""); especialidad.setText("");
        nombre.requestFocusInWindow();
    }

    public void actualizar() { }
}
