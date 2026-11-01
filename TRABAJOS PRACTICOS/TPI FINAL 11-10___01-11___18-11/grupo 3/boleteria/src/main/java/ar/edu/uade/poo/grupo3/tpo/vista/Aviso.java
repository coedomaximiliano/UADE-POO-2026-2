package ar.edu.uade.poo.grupo3.tpo.vista;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;

/** Mensaje dentro de la pantalla (éxito, error o información): un JLabel con fondo de color. */
public class Aviso extends JLabel {

    private static final Color EXITO = new Color(0x1E7D3A);
    private static final Color EXITO_FONDO = new Color(0xE4F5E8);
    private static final Color ERROR = new Color(0xB42318);
    private static final Color ERROR_FONDO = new Color(0xFBE9E7);
    private static final Color INFO = new Color(0x8A5A00);
    private static final Color INFO_FONDO = new Color(0xFFF4D6);

    public Aviso() {
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setVisible(false);
    }

    public void exito(String mensaje) {
        mostrar(mensaje, EXITO, EXITO_FONDO);
    }

    public void error(String mensaje) {
        mostrar(mensaje, ERROR, ERROR_FONDO);
    }

    public void info(String mensaje) {
        mostrar(mensaje, INFO, INFO_FONDO);
    }

    public void ocultar() {
        setVisible(false);
    }

    private void mostrar(String mensaje, Color texto, Color fondo) {
        setForeground(texto);
        setBackground(fondo);
        setText(mensaje);
        setVisible(true);
    }
}
