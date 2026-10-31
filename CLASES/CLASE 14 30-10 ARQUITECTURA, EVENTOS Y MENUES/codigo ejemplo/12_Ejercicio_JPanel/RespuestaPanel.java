import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Diapositiva 32 - Respuesta ejercicio: JPanel (cambiar color rojo/azul)
public class RespuestaPanel {
    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Ejercicio JPanel");
        ventana.setSize(400, 300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        // Crear panel
        JPanel panel = new JPanel();
        panel.setBounds(50, 30, 300, 150);
        panel.setBackground(Color.LIGHT_GRAY);
        ventana.add(panel);

        // Botón rojo
        JButton botonRojo = new JButton("Rojo");
        botonRojo.setBounds(70, 200, 100, 30);
        ventana.add(botonRojo);

        // Botón azul
        JButton botonAzul = new JButton("Azul");
        botonAzul.setBounds(220, 200, 100, 30);
        ventana.add(botonAzul);

        // Acciones de los botones
        botonRojo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(Color.RED);
            }
        });

        botonAzul.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(Color.BLUE);
            }
        });

        // Mostrar ventana
        ventana.setVisible(true);
    }
}
