import javax.swing.*;

// Diapositiva 40 - Hilo de ejecución de las interfaces gráficas (EDT)
public class HiloGUI {
    public static void main(String[] args) {
        // Ejecutar el código de GUI en el hilo EDT
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame ventana = new JFrame("Ventana segura en EDT");
                ventana.setSize(300, 200);
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel etiqueta = new JLabel("Hola desde el EDT");
                ventana.add(etiqueta);

                ventana.setVisible(true);
            }
        });
    }
}
