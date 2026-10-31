import javax.swing.*;
import java.awt.*;

// Diapositiva 9 - Componentes de la GUI: formulario simple
public class Main {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Formulario");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new FlowLayout());
        JLabel etiqueta = new JLabel("Ingresa tu nombre:");
        JTextField campoTexto = new JTextField(15);
        JButton boton = new JButton("Saludar");
        boton.addActionListener(e -> {
            String nombre = campoTexto.getText();
            JOptionPane.showMessageDialog(null, "Hola, " + nombre + "!");
        });
        ventana.add(etiqueta);
        ventana.add(campoTexto);
        ventana.add(boton);
        ventana.setVisible(true);
    }
}
