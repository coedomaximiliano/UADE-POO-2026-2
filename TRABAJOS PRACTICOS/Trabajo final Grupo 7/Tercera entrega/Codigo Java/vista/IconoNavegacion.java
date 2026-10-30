package vista;

import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.Icon;

public class IconoNavegacion implements Icon {
    private String tipo;
    private int tamano;

    public IconoNavegacion(String tipo, int tamano) {
        this.tipo = tipo; this.tamano = tamano;
    }

    public int getIconWidth() { return tamano; }
    public int getIconHeight() { return tamano; }

    public void paintIcon(Component componente, Graphics grafico, int x, int y) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.translate(x, y); g.scale(tamano / 64.0, tamano / 64.0);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(EstiloClinica.TEXTO);
        g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (tipo.equals("ticket")) {
            // El ticket tiene la muesca de arriba y la curva de abajo de la referencia.
            Path2D ticket = new Path2D.Double();
            ticket.moveTo(10, 6); ticket.curveTo(21, 6, 23, 12, 25, 18);
            ticket.curveTo(28, 28, 38, 28, 41, 18);
            ticket.curveTo(43, 12, 45, 6, 54, 6); ticket.lineTo(54, 44);
            ticket.curveTo(44, 46, 42, 52, 39, 57);
            ticket.curveTo(36, 63, 27, 63, 24, 57);
            ticket.curveTo(21, 52, 20, 46, 10, 44); ticket.closePath();
            g.draw(ticket);
            g.setFont(new Font("SansSerif", Font.BOLD, 17)); g.drawString("001", 16, 42);
        } else {
            g.drawRoundRect(7, 12, 50, 45, 8, 8);
            g.drawLine(19, 5, 19, 19); g.drawLine(45, 5, 45, 19);
            g.drawLine(8, 26, 56, 26);
            for (int fila = 0; fila < 2; fila++) {
                for (int columna = 0; columna < 3; columna++) {
                    g.fillRoundRect(16 + columna * 12, 34 + fila * 11, 6, 6, 2, 2);
                }
            }
        }
        g.dispose();
    }
}
