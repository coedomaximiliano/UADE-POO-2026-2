import javax.swing.*;
import java.awt.*;

// Diapositivas 34 a 38 - Uso de diálogos Swing (JOptionPane)
// Cada botón muestra uno de los diálogos de las diapositivas.
// La ventana (this) es el componente padre, por eso los diálogos son modales.
public class EjemploDialogos extends JFrame {

    public EjemploDialogos() {
        super("Diálogos Swing");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 8, 8));

        agregarBoton("Error", this::mensajeError);
        agregarBoton("Información", this::mensajeInformacion);
        agregarBoton("Alerta", this::mensajeAlerta);
        agregarBoton("Pregunta", this::mensajePregunta);
        agregarBoton("Mensaje plano", this::mensajePlano);
        agregarBoton("Confirmación 1 (Sí/No)", this::confirmacion1);
        agregarBoton("Confirmación 2 (Sí/No/Cancelar)", this::confirmacion2);
        agregarBoton("Confirmación 3 (Aceptar/Cancelar)", this::confirmacion3);
    }

    private void agregarBoton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        add(boton);
    }

    // Diapositiva 34 - Mensaje de Error e Información
    private void mensajeError() {
        JOptionPane.showMessageDialog(this, "Mensaje de error",
                "titulo ventana error", JOptionPane.ERROR_MESSAGE);
    }

    private void mensajeInformacion() {
        JOptionPane.showMessageDialog(this, "Mensaje informacion",
                "titulo ventana info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Diapositiva 35 - Mensaje de Alerta y Pregunta
    private void mensajeAlerta() {
        JOptionPane.showMessageDialog(this, "Mensaje de alerta",
                "titulo ventana alert", JOptionPane.WARNING_MESSAGE);
    }

    private void mensajePregunta() {
        JOptionPane.showMessageDialog(this, "Mensaje de pregunta",
                "titulo ventana pregunta", JOptionPane.QUESTION_MESSAGE);
    }

    // Diapositiva 36 - Mensaje Plano y con Opciones
    private void mensajePlano() {
        JOptionPane.showMessageDialog(this, "Mensaje comun", "titulo ventana ",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void confirmacion1() {
        int resultado1 = JOptionPane.showConfirmDialog(this,
                "Esta seguro de...", "Confirmacion 1", JOptionPane.YES_NO_OPTION);
        if (resultado1 == JOptionPane.YES_OPTION) {
            System.out.println("el usuario dijo si a la confirmacion 1");
        }

        if (resultado1 == JOptionPane.NO_OPTION) {
            System.out.println("el usuario dijo no a la confirmacion 1");
        }
    }

    // Diapositivas 37 y 38 - Otros Diálogos de Mensajes con Opciones
    private void confirmacion2() {
        int resultado2 = JOptionPane.showConfirmDialog(this,
                "Esta seguro de...", "Confirmacion 2",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (resultado2 == JOptionPane.YES_OPTION) {
            System.out.println("el usuario dijo si a la confirmacion 2");
        }
        if (resultado2 == JOptionPane.NO_OPTION) {
            System.out.println("el usuario dijo no a la confirmacion 2");
        }
        if (resultado2 == JOptionPane.CANCEL_OPTION) {
            System.out.println("el usuario dijo cancelar a la confirmacion 2");
        }
    }

    private void confirmacion3() {
        int resultado3 = JOptionPane.showConfirmDialog(this,
                "Esta seguro de...", "Confirmacion 3",
                JOptionPane.OK_CANCEL_OPTION);
        if (resultado3 == JOptionPane.OK_OPTION) {
            System.out.println("el usuario dijo OK a la confirmacion 3");
        }
        if (resultado3 == JOptionPane.CANCEL_OPTION) {
            System.out.println("el usuario dijo cancelar a la confirmacion 3");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploDialogos().setVisible(true));
    }
}
