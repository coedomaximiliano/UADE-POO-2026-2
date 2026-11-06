package aplicacion;

import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class InicioSql {
    public static void main(String[] args) {
        try {
            Path jar =
                    Path.of(
                            InicioSql.class
                                    .getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .toURI());
            Path carpeta = jar.getParent();
            Path ejecutable = Path.of(System.getProperty("java.home"), "bin", "javaw.exe");
            ArrayList<String> comando = new ArrayList<String>();
            comando.add(ejecutable.toString());
            comando.add("-Djava.library.path=" + carpeta.resolve("lib"));
            comando.add("-Dclinica.dir=" + carpeta);
            comando.add("-cp");
            comando.add(
                    jar
                            + java.io.File.pathSeparator
                            + (carpeta.resolve("lib") + java.io.File.separator + "*"));
            comando.add("aplicacion.Main");
            for (String argumento : args) comando.add(argumento);
            // Se inicia con el controlador y la autenticacion de Windows de lib.
            new ProcessBuilder(comando).directory(carpeta.toFile()).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo iniciar la aplicacion: " + e.getMessage(),
                    "Inicio de la clinica",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
