package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Factura;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;

/** Diálogo modal que muestra el comprobante emitido al confirmar el pago. */
public class FacturaDialog extends JDialog {

    public FacturaDialog(Window padre, Factura factura) {
        super(padre, "Comprobante " + factura.getNumero(), ModalityType.APPLICATION_MODAL);
        setSize(520, 640);
        setLocationRelativeTo(padre);

        JLabel titulo = Estilo.titulo("Compra confirmada");
        titulo.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JTextArea texto = new JTextArea(factura.generarTexto());
        texto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        texto.setEditable(false);
        texto.setMargin(new java.awt.Insets(12, 16, 12, 16));

        JButton cerrar = new JButton("Listo");
        cerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        pie.add(Estilo.subtitulo("Guardado en datos/facturas/FAC-" + factura.getNumero() + ".txt"), BorderLayout.WEST);
        pie.add(cerrar, BorderLayout.EAST);

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(texto), BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
    }
}
