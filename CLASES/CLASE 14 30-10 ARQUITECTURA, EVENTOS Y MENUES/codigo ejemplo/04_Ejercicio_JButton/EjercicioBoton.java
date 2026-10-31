import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Diapositiva 16 - Respuesta ejercicio: JButton (contador de clics)
public class EjercicioBoton {
    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Contador de clics");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        // Etiqueta para mostrar el contador
        JLabel etiqueta = new JLabel("Clics: 0");
        etiqueta.setBounds(120, 30, 100, 30);
        ventana.add(etiqueta);

        // Botón para incrementar el contador
        JButton boton = new JButton("Haz clic");
        boton.setBounds(100, 80, 100, 30);
        ventana.add(boton);

        // Variable para contar los clics
        final int[] contador = {0};

        // Acción del botón
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contador[0]++;
                etiqueta.setText("Clics: " + contador[0]);
            }
        });

        // Mostrar ventana
        ventana.setVisible(true);
    }
}
