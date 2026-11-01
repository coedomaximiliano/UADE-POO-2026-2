package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.DatosIniciales;
import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.IOException;

/** Punto de entrada de la aplicación Swing. */
public final class BoleteriaApp {

    private BoleteriaApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estilo.aplicarGlobal();
            Boleteria boleteria = Boleteria.getInstancia();
            try {
                DatosIniciales.inicializar(boleteria);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "No se pudieron leer los archivos de la carpeta datos/:\n" + ex.getMessage(),
                        "Boletería", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new VentanaPrincipal(boleteria).setVisible(true);
        });
    }
}
