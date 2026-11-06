package aplicacion;

import vista.EstiloClinica;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    // El ejecutable inicia esta clase con el controlador JDBC en lib.
    // La configuracion de la base esta en clinica.properties.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception e) {
                            // Si no encuentra el estilo de Windows usa el que trae Java.
                            System.out.println("Se usa la apariencia predeterminada de Swing.");
                        }
                        EstiloClinica.configurar();
                        VentanaPrincipal ventana;
                        try {
                            ventana = new VentanaPrincipal(new AplicacionClinica());
                        } catch (IllegalArgumentException e) {
                            javax.swing.JOptionPane.showMessageDialog(
                                    null,
                                    e.getMessage(),
                                    "No se pudo abrir la base SQL",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (args.length > 0 && args[0].equalsIgnoreCase("calendario")) {
                            ventana.abrirAgenda();
                        }
                        ventana.setVisible(true);
                    }
                });
    }
}
