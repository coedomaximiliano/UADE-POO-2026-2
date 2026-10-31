import javax.swing.*;
import java.awt.*;
import java.util.List;

// Diapositiva 42 - SwingWorker: contador que actualiza un JLabel cada segundo
public class EjemploSwingWorker {
    public static void main(String[] args) {
        // Ejecutar GUI en EDT
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Ejemplo SwingWorker");
            ventana.setSize(300, 200);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLayout(new FlowLayout());

            JLabel etiqueta = new JLabel("Contador: 0");
            ventana.add(etiqueta);

            JButton boton = new JButton("Iniciar contador");
            ventana.add(boton);
            boton.addActionListener(e -> {
                boton.setEnabled(false); // Deshabilitar botón durante el conteo
                // Crear y ejecutar SwingWorker
                new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (int i = 1; i <= 10; i++) {
                            Thread.sleep(1000); // Esperar 1 segundo
                            publish(i); // Enviar el valor i para actualizar la GUI
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<Integer> chunks) {
                        // Actualizar JLabel con el último valor recibido
                        int valor = chunks.get(chunks.size() - 1);
                        etiqueta.setText("Contador: " + valor);
                    }

                    @Override
                    protected void done() {
                        boton.setEnabled(true); // Rehabilitar el botón cuando termine
                        etiqueta.setText("¡Conteo terminado!");
                    }
                }.execute();
            });

            ventana.setVisible(true);
        });
    }
}
