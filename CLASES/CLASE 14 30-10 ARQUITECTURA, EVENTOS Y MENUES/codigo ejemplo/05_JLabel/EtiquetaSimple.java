import javax.swing.*;

// Diapositiva 18 - JLabel (etiqueta)
public class EtiquetaSimple {
    public static void main(String[] args) {
        // Crear ventana
        JFrame ventana = new JFrame("Ejemplo JLabel");
        ventana.setSize(300, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null); // Usamos posiciones absolutas

        // Crear etiqueta
        JLabel etiqueta = new JLabel("¡Hola, soy una etiqueta!");
        etiqueta.setBounds(70, 80, 200, 30); // x, y, ancho, alto

        // Agregar la etiqueta a la ventana
        ventana.add(etiqueta);

        // Hacer visible la ventana
        ventana.setVisible(true);
    }
}
