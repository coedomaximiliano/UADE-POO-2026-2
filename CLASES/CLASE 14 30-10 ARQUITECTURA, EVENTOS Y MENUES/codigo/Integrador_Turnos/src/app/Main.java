package app;

import controlador.TurnoControlador;
import datos.TurnoRepositorio;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import vista.VentanaTurnos;

// Arma la aplicacion: datos -> controlador -> vista
public class Main {
    public static void main(String[] args) {
        TurnoControlador ctrl = new TurnoControlador(new TurnoRepositorio("turnos.txt"));
        try {
            ctrl.iniciar();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudieron leer los turnos guardados");
        }
        SwingUtilities.invokeLater(() -> new VentanaTurnos(ctrl).setVisible(true));
    }
}
