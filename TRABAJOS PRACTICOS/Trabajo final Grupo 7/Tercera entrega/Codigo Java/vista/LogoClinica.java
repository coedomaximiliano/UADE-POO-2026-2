package vista;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

public class LogoClinica implements Icon {
    private int tamano;

    public LogoClinica(int tamano) { this.tamano = tamano; }
    public int getIconWidth() { return tamano; }
    public int getIconHeight() { return tamano; }

    public void paintIcon(Component componente, Graphics grafico, int x, int y) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.translate(x, y); g.scale(tamano / 64.0, tamano / 64.0);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(EstiloClinica.PRIMARIO);
        g.fillRoundRect(3, 8, 58, 53, 14, 14);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(18, 4, 18, 16); g.drawLine(46, 4, 46, 16);
        g.drawLine(14, 24, 50, 24);
        Path2D marca = new Path2D.Double();
        marca.moveTo(19, 41); marca.lineTo(28, 49); marca.lineTo(46, 33);
        g.draw(marca); g.dispose();
    }

    public BufferedImage imagen() {
        BufferedImage imagen = new BufferedImage(tamano, tamano, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics(); paintIcon(null, g, 0, 0); g.dispose();
        return imagen;
    }
}
