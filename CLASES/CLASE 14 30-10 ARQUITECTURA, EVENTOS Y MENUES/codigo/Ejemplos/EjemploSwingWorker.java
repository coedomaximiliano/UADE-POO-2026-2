import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

// Tarea larga fuera del EDT: la ventana NO se congela
public class EjemploSwingWorker extends JFrame {
    private JLabel lbl = new JLabel("Listo", JLabel.CENTER);
    private JButton btn = new JButton("Contar hasta 10");

    public EjemploSwingWorker() {
        super("SwingWorker");
        setSize(300, 120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(lbl, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);
        btn.addActionListener(e -> iniciar());
    }

    private void iniciar() {
        btn.setEnabled(false);
        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {   // otro hilo
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(1000);
                    publish(i);                                 // envia al EDT
                }
                return null;
            }
            @Override
            protected void process(List<Integer> valores) {      // en el EDT
                lbl.setText("Contando: " + valores.get(valores.size() - 1));
            }
            @Override
            protected void done() {                              // en el EDT
                lbl.setText("Termino!");
                btn.setEnabled(true);
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploSwingWorker().setVisible(true));
    }
}
