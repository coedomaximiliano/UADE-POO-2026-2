import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class EditorSimple extends JFrame {
    private JTextArea area = new JTextArea();

    public EditorSimple() {
        super("Editor simple");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JScrollPane(area));
        setJMenuBar(crearMenu());
    }

    private JMenuBar crearMenu() {
        JMenu archivo = new JMenu("Archivo");
        archivo.add(item("Nuevo", KeyEvent.VK_N, () -> area.setText("")));
        archivo.add(item("Abrir...", KeyEvent.VK_O, this::abrir));
        archivo.add(item("Guardar...", KeyEvent.VK_S, this::guardar));
        archivo.addSeparator();
        archivo.add(item("Salir", KeyEvent.VK_Q, this::dispose));

        JMenu ayuda = new JMenu("Ayuda");
        ayuda.add(item("Acerca de", KeyEvent.VK_F1, () ->
            JOptionPane.showMessageDialog(this, "Editor POO - Clase 14")));

        JMenuBar barra = new JMenuBar();
        barra.add(archivo);
        barra.add(ayuda);
        return barra;
    }

    // Metodo auxiliar: crea un item con atajo Ctrl+tecla y su accion
    private JMenuItem item(String texto, int tecla, Runnable accion) {
        JMenuItem it = new JMenuItem(texto);
        it.setAccelerator(KeyStroke.getKeyStroke(tecla, InputEvent.CTRL_DOWN_MASK));
        it.addActionListener(e -> accion.run());
        return it;
    }

    private void abrir() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try (BufferedReader in = new BufferedReader(new FileReader(f))) {
            area.read(in, null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fc.getSelectedFile()))) {
            area.write(out);
            JOptionPane.showMessageDialog(this, "Archivo guardado");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditorSimple().setVisible(true));
    }
}
