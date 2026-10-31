import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Diapositiva 24 - Respuesta ejercicio: JTextField
public class EjercicioMostrarTexto {
    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Mostrar texto");
        ventana.setSize(350, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        // Campo de texto
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(50, 30, 200, 30);
        ventana.add(campoTexto);

        // Botón
        JButton boton = new JButton("Mostrar texto");
        boton.setBounds(100, 70, 130, 30);
        ventana.add(boton);

        // Etiqueta
        JLabel etiqueta = new JLabel("Texto aquí");
        etiqueta.setBounds(50, 120, 250, 30);
        ventana.add(etiqueta);

        // Acción del botón
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = campoTexto.getText();
                etiqueta.setText("Ingresaste: " + texto);
            }
        });

        // Mostrar ventana
        ventana.setVisible(true);
    }
}
