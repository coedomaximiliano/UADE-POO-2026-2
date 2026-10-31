import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

// Tres formas de escuchar el mismo evento (ActionEvent de un JButton)
public class EjemploEventos extends JFrame {
    private JLabel lbl = new JLabel("Presione un boton");

    // 1) Clase interna con nombre que implementa ActionListener
    private class OyenteRojo implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            lbl.setText("Clase interna: " + e.getActionCommand());
        }
    }

    public EjemploEventos() {
        super("Manejo de eventos");
        setLayout(new FlowLayout());
        setSize(420, 120);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton b1 = new JButton("Rojo");
        JButton b2 = new JButton("Verde");
        JButton b3 = new JButton("Azul");

        b1.addActionListener(new OyenteRojo());

        // 2) Clase anonima
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbl.setText("Clase anonima: " + e.getActionCommand());
            }
        });

        // 3) Expresion lambda (ActionListener tiene un solo metodo)
        b3.addActionListener(e -> lbl.setText("Lambda: " + e.getActionCommand()));

        add(b1); add(b2); add(b3); add(lbl);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploEventos().setVisible(true));
    }
}
