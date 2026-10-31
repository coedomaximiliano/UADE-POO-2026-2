import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Eventos de teclado, mouse y ventana (usando clases Adapter)
public class EjemploOtrosEventos extends JFrame {
    public EjemploOtrosEventos() {
        super("Teclado, mouse y ventana");
        setSize(400, 160);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JTextField txt = new JTextField();
        JLabel lbl = new JLabel("Pase el mouse por aca", JLabel.CENTER);

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                lbl.setText("Caracteres escritos: " + txt.getText().length());
            }
        });
        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { lbl.setText("Mouse adentro"); }
            @Override
            public void mouseExited(MouseEvent e) { lbl.setText("Mouse afuera"); }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int r = JOptionPane.showConfirmDialog(EjemploOtrosEventos.this, "Desea salir?");
                if (r == JOptionPane.YES_OPTION) System.exit(0);
            }
        });
        add(txt, BorderLayout.NORTH);
        add(lbl, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploOtrosEventos().setVisible(true));
    }
}
