import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Diapositiva 20 - Respuesta ejercicio: JLabel
public class CambiarEtiqueta {
    public static void main(String[] args) {
        // Crear la ventana
        JFrame ventana = new JFrame("Ejercicio JLabel");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null); // Posiciones absolutas

        // Crear una etiqueta
        JLabel etiqueta = new JLabel("Texto original");
        etiqueta.setBounds(90, 40, 150, 30);
        ventana.add(etiqueta);

        // Crear un botón
        JButton boton = new JButton("Cambiar texto");
        boton.setBounds(80, 90, 140, 30);
        ventana.add(boton);

        // Acción del botón
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                etiqueta.setText("¡Texto cambiado!");
            }
        });

        // Mostrar la ventana
        ventana.setVisible(true);
    }
}
