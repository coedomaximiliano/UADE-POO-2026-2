package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

/** Alta de cuenta de cliente: persiste en datos/clientes.txt vía {@link Boleteria#guardarClientes()}. */
public class PanelRegistroCliente extends JPanel implements Pantalla {

    private final Boleteria boleteria;
    private final JTextField dni = new JTextField(16);
    private final JTextField nombre = new JTextField(24);
    private final JTextField email = new JTextField(24);
    private final JPasswordField contrasenia = new JPasswordField(16);
    private final Aviso aviso = new Aviso();

    public PanelRegistroCliente(Boleteria boleteria) {
        super(new BorderLayout());
        this.boleteria = boleteria;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Registro de cliente",
                "Creá una cuenta con email y contraseña para comprar entradas en la pantalla de venta."),
                BorderLayout.NORTH);

        JPanel formulario = Estilo.columna(Estilo.seccion("Datos de la cuenta"),
                new JLabel("DNI"), dni,
                new JLabel("Nombre completo"), nombre,
                new JLabel("Email"), email,
                new JLabel("Contraseña"), contrasenia);
        JButton registrar = new JButton("Registrar cuenta");
        registrar.addActionListener(e -> registrar());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.add(registrar);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.add(Estilo.tarjeta(formulario), BorderLayout.NORTH);
        centro.add(acciones, BorderLayout.CENTER);
        centro.add(aviso, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
    }

    @Override
    public void alMostrar() {
        aviso.ocultar();
    }

    private void registrar() {
        aviso.ocultar();
        String dniTxt = dni.getText().trim();
        String nombreTxt = nombre.getText().trim();
        String emailTxt = email.getText().trim();
        String clave = new String(contrasenia.getPassword());
        if (dniTxt.isEmpty() || nombreTxt.isEmpty() || emailTxt.isEmpty() || clave.isEmpty()) {
            aviso.error("Completá todos los campos.");
            return;
        }
        try {
            boleteria.registrarCuenta(dniTxt, nombreTxt, emailTxt, clave);
            boleteria.guardarClientes();
            aviso.exito("Cuenta registrada para " + emailTxt + ". Iniciá sesión en Clientes → Iniciar sesión.");
            dni.setText("");
            nombre.setText("");
            email.setText("");
            contrasenia.setText("");
        } catch (IllegalArgumentException e) {
            aviso.error(e.getMessage());
        } catch (IOException e) {
            aviso.error("No se pudo guardar en disco: " + e.getMessage());
        }
    }
}
