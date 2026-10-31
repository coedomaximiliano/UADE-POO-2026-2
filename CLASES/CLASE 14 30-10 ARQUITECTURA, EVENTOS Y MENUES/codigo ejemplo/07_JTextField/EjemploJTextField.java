import javax.swing.*;

// Diapositiva 22 - JTextField (campo de texto)
public class EjemploJTextField {
    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Ejemplo JTextField");
        ventana.setSize(300, 150);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        // Crear JTextField
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(50, 50, 200, 30);

        // Agregar a la ventana
        ventana.add(campoTexto);

        // Mostrar la ventana
        ventana.setVisible(true);
    }
}
