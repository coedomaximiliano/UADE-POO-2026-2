package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;
import ar.edu.uade.poo.grupo3.tpo.modelo.ventas.Cliente;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Optional;

/** Inicio de sesión del cliente para comprar en {@link PanelVenta} con su cuenta. */
public class PanelLoginCliente extends JPanel implements Pantalla {

    private final Boleteria boleteria;
    private final JTextField email = new JTextField(24);
    private final JPasswordField contrasenia = new JPasswordField(16);
    private final JLabel sesionActiva = Estilo.subtitulo(" ");
    private final Aviso aviso = new Aviso();

    public PanelLoginCliente(Boleteria boleteria) {
        super(new BorderLayout());
        this.boleteria = boleteria;
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        add(Estilo.encabezado("Iniciar sesión",
                "Ingresá con el email y la contraseña de tu cuenta. La venta de entradas usa el cliente logueado."),
                BorderLayout.NORTH);

        JPanel formulario = Estilo.columna(Estilo.seccion("Credenciales"),
                new JLabel("Email"), email,
                new JLabel("Contraseña"), contrasenia,
                sesionActiva);
        JButton ingresar = new JButton("Iniciar sesión");
        ingresar.addActionListener(e -> ingresar());
        JButton cerrar = new JButton("Cerrar sesión");
        cerrar.addActionListener(e -> cerrarSesion());
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.add(ingresar);
        acciones.add(cerrar);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.add(Estilo.tarjeta(formulario), BorderLayout.NORTH);
        centro.add(acciones, BorderLayout.CENTER);
        centro.add(aviso, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
    }

    @Override
    public void alMostrar() {
        aviso.ocultar();
        actualizarSesionActiva();
    }

    private void actualizarSesionActiva() {
        Optional<Cliente> sesion = boleteria.getClienteSesion();
        if (sesion.isPresent()) {
            Cliente c = sesion.get();
            sesionActiva.setText("Sesión activa: " + c.getNombre() + " · " + c.getEmail());
        } else {
            sesionActiva.setText("No hay sesión iniciada.");
        }
    }

    private void ingresar() {
        aviso.ocultar();
        String emailTxt = email.getText().trim();
        String clave = new String(contrasenia.getPassword());
        if (emailTxt.isEmpty() || clave.isEmpty()) {
            aviso.error("Completá email y contraseña.");
            return;
        }
        Optional<Cliente> cliente = boleteria.iniciarSesion(emailTxt, clave);
        if (cliente.isEmpty()) {
            aviso.error("Email o contraseña incorrectos.");
            return;
        }
        contrasenia.setText("");
        actualizarSesionActiva();
        aviso.exito("Bienvenido/a, " + cliente.get().getNombre()
                + ". Podés ir a Ventas → Venta de entradas.");
    }

    private void cerrarSesion() {
        boleteria.cerrarSesion();
        actualizarSesionActiva();
        aviso.info("Sesión cerrada.");
    }
}
