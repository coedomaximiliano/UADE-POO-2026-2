import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Diapositiva 14 - JButton (botón)
public class BotonSimple {
    public static void main(String[] args) {
        // Crear la ventana
        JFrame ventana = new JFrame("Ejemplo JButton");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null); // Diseño libre (sin layout manager)

        // Crear un botón
        JButton boton = new JButton("Haz clic");
        boton.setBounds(100, 70, 100, 30); // x, y, ancho, alto

        // Agregar una acción al botón
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "¡Botón presionado!");
            }
        });

        // Agregar el botón a la ventana
        ventana.add(boton);

        // Mostrar la ventana
        ventana.setVisible(true);
    }
}
