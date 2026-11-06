package vista;

import aplicacion.AplicacionClinica;

import modelo.Medico;

import java.awt.*;

import javax.swing.*;

public class PanelAltaMedico extends PanelPantalla {
    private JTextField nombre = campo("nombreMedico"),
            dni = campo("dniMedico"),
            email = campo("emailMedico");
    private JTextField matricula = campo("matriculaMedico"),
            especialidad = campo("especialidadMedico"),
            consultorio = campo("consultorioMedico");
    private EditorHorarioMedico horarios = new EditorHorarioMedico();
    private Runnable alGuardar;

    public PanelAltaMedico(AplicacionClinica app) {
        this(app, null);
    }

    public PanelAltaMedico(AplicacionClinica app, Runnable alGuardar) {
        super(
                app,
                "Registrar médico",
                "Completá los datos y marcá los días y horarios de atención. Turnos de 30 minutos.");
        this.alGuardar = alGuardar;
        JPanel datos =
                formulario(
                        new String[] {
                            "Nombre *",
                            "DNI *",
                            "Correo",
                            "Matrícula *",
                            "Especialidad *",
                            "Consultorio *"
                        },
                        new JTextField[] {
                            nombre, dni, email, matricula, especialidad, consultorio
                        });
        JPanel centro = new JPanel(new GridLayout(1, 2, 25, 0));
        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.add(datos, BorderLayout.NORTH);
        centro.add(izquierda);
        centro.add(horarios);
        add(centro, BorderLayout.CENTER);
        add(
                acciones(
                        boton("Registrar médico", "guardarMedico", e -> guardar()),
                        boton("Limpiar", "limpiarMedico", e -> limpiar())),
                BorderLayout.SOUTH);
    }

    private void guardar() {
        try {
            app.getMedicos()
                    .agregar(
                            new Medico(
                                    nombre.getText(),
                                    dni.getText(),
                                    email.getText(),
                                    matricula.getText(),
                                    especialidad.getText(),
                                    consultorio.getText(),
                                    horarios.getHorarios()));
            informar("Médico y horarios registrados correctamente.");
            limpiar();
            if (alGuardar != null) alGuardar.run();
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    private void limpiar() {
        for (JTextField c :
                new JTextField[] {nombre, dni, email, matricula, especialidad, consultorio})
            c.setText("");
        horarios.setHorarios(new java.util.ArrayList<modelo.HorarioAtencion>());
        nombre.requestFocusInWindow();
    }

    public void actualizar() {}
}
