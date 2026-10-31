import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class VentanaDescarga extends JFrame {
    private JProgressBar barra = new JProgressBar(0, 100);
    private JLabel lblEstado = new JLabel("Esperando...", JLabel.CENTER);
    private JButton btnIniciar = new JButton("Iniciar");
    private JButton btnCancelar = new JButton("Cancelar");
    private SwingWorker<Void, Void> tarea;

    public VentanaDescarga() {
        super("Descarga simulada");
        setSize(360, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        barra.setStringPainted(true);
        btnCancelar.setEnabled(false);

        JPanel botones = new JPanel();
        botones.add(btnIniciar);
        botones.add(btnCancelar);
        add(lblEstado, BorderLayout.NORTH);
        add(barra, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnIniciar.addActionListener(e -> iniciar());
        btnCancelar.addActionListener(e -> tarea.cancel(true));
    }

    private void iniciar() {
        btnIniciar.setEnabled(false);
        btnCancelar.setEnabled(true);
        lblEstado.setText("Descargando...");
        tarea = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100 && !isCancelled(); i += 5) {
                    Thread.sleep(200);          // simula trabajo pesado
                    setProgress(i);             // dispara un PropertyChangeEvent
                }
                return null;
            }
            @Override
            protected void done() {
                lblEstado.setText(isCancelled() ? "Cancelado" : "Descarga completa");
                btnIniciar.setEnabled(true);
                btnCancelar.setEnabled(false);
            }
        };
        tarea.addPropertyChangeListener(ev -> {
            if ("progress".equals(ev.getPropertyName())) {
                barra.setValue((Integer) ev.getNewValue());
            }
        });
        tarea.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaDescarga().setVisible(true));
    }
}
