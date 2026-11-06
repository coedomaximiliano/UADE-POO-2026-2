package aplicacion;

import vista.EstiloClinica;
import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    // Compilar todos los archivos: javac -encoding UTF-8 *.java
    // Abrir la ventana: java Main
    // La prueba anterior sigue disponible: java MainConsola demo
    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equalsIgnoreCase("demo") || args[0].equalsIgnoreCase("consola"))) {
            MainConsola.main(args[0].equalsIgnoreCase("demo") ? new String[]{"demo"} : new String[0]);
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // Si no encuentra el estilo de Windows usa el que trae Java.
                    System.out.println("Se usa la apariencia predeterminada de Swing.");
                }
                EstiloClinica.configurar();
                VentanaPrincipal ventana = new VentanaPrincipal(new AplicacionClinica());
                if (args.length > 0 && args[0].equalsIgnoreCase("calendario")) {
                    ventana.abrirAgenda();
                }
                ventana.setVisible(true);
            }
        });
    }
}
