import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VentanaContador extends JFrame {
    private int valor = 0;
    private JLabel lblValor = new JLabel("0", JLabel.CENTER);
    private JButton btnMas = new JButton("+1");
    private JButton btnMenos = new JButton("-1");
    private JButton btnReset = new JButton("Reiniciar");

    public VentanaContador() {
        super("Contador");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        lblValor.setFont(new Font("SansSerif", Font.BOLD, 40));
        JPanel botones = new JPanel();
        botones.add(btnMenos);
        botones.add(btnMas);
        botones.add(btnReset);
        add(lblValor, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnMas.addActionListener(e -> cambiar(1));
        btnMenos.addActionListener(e -> cambiar(-1));
        btnReset.addActionListener(e -> { valor = 0; actualizar(); });
    }

    private void cambiar(int delta) {
        valor += delta;
        actualizar();
    }

    private void actualizar() {
        lblValor.setText(String.valueOf(valor));
        btnMenos.setEnabled(valor > 0);        // no permitir negativos
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaContador().setVisible(true));
    }
}
