import javax.swing.*;
import java.awt.*;

// Diapositiva 30 - JPanel (panel de organización)
public class EjemploJPanel {
    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Ejemplo JPanel");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);  // Color de fondo

        // Agregar el panel a la ventana
        ventana.add(panel);

        // Mostrar la ventana
        ventana.setVisible(true);
    }
}
