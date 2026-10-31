import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

// Barra de menu: JMenuBar -> JMenu -> JMenuItem
public class EjemploMenu extends JFrame {
    public EjemploMenu() {
        super("Menues en Swing");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenu ayuda = new JMenu("Ayuda");

        JMenuItem nuevo = new JMenuItem("Nuevo");
        nuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                             java.awt.event.InputEvent.CTRL_DOWN_MASK));  // Ctrl+N
        JMenuItem salir = new JMenuItem("Salir");
        JMenuItem acerca = new JMenuItem("Acerca de...");

        nuevo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Nuevo documento"));
        salir.addActionListener(e -> dispose());
        acerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Ejemplo de menu - POO UADE", "Acerca de", JOptionPane.INFORMATION_MESSAGE));

        archivo.add(nuevo);
        archivo.addSeparator();
        archivo.add(salir);
        ayuda.add(acerca);
        barra.add(archivo);
        barra.add(ayuda);
        setJMenuBar(barra);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EjemploMenu().setVisible(true));
    }
}
