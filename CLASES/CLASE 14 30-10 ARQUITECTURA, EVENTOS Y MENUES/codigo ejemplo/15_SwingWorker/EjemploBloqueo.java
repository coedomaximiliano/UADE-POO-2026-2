
import javax.swing.*;
import java.awt.*;

public class EjemploBloqueo {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame ventana = new JFrame("EDT vs SwingWorker");
            ventana.setSize(450, 250);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLayout(new FlowLayout());

            JLabel etiqueta = new JLabel("Contador: 0");
            JTextField input = new JTextField(20);

            JButton botonBloquear = new JButton("Bloquear interfaz");
            JButton botonNoBloquear = new JButton("No bloquear");

            ventana.add(etiqueta);
            ventana.add(new JLabel("Escribí aquí:"));
            ventana.add(input);
            ventana.add(botonBloquear);
            ventana.add(botonNoBloquear);

            // BOTON 1: BLOQUEA EL EDT
            botonBloquear.addActionListener(e -> {

                for (int i = 1; i <= 10; i++) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    etiqueta.setText("Contador: " + i);
                }

                etiqueta.setText("¡Terminado!");
            });

            // BOTON 2: UTILIZA SWINGWORKER
            botonNoBloquear.addActionListener(e -> {

                new SwingWorker<Void, Integer>() {

                    @Override
                    protected Void doInBackground() throws Exception {

                        for (int i = 1; i <= 10; i++) {

                            Thread.sleep(1000);
                            publish(i);
                        }

                        return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> valores) {

                        int ultimo = valores.get(valores.size() - 1);
                        etiqueta.setText("Contador: " + ultimo);
                    }

                    @Override
                    protected void done() {
                        etiqueta.setText("¡Terminado!");
                    }

                }.execute();
            });

            ventana.setVisible(true);
        });
    }
}
