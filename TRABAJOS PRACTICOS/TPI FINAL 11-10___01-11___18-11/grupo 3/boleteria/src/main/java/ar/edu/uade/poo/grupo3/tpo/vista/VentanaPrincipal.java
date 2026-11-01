package ar.edu.uade.poo.grupo3.tpo.vista;

import ar.edu.uade.poo.grupo3.tpo.controlador.SedeController;
import ar.edu.uade.poo.grupo3.tpo.controlador.VentaController;
import ar.edu.uade.poo.grupo3.tpo.modelo.Boleteria;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/** Ventana principal: una barra de menú para navegar y las pantallas apiladas en un CardLayout. */
public class VentanaPrincipal extends JFrame {

    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final Map<String, Pantalla> pantallas = new LinkedHashMap<>();

    public VentanaPrincipal(Boleteria boleteria) {
        super("Boletería · TPO Grupo 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 760);
        setLocationRelativeTo(null);

        SedeController sedes = new SedeController(boleteria);
        VentaController ventas = new VentaController(boleteria);
        registrar("inicio", new PanelInicio(boleteria, () -> ir("alta"), () -> ir("consulta"), () -> ir("registro")));
        registrar("consulta", new PanelConsultaSedes(sedes));
        registrar("alta", new PanelAltaSede(sedes));
        registrar("modificacion", new PanelModificacionSede(sedes));
        registrar("baja", new PanelBajaSede(sedes));
        registrar("registro", new PanelRegistroCliente(boleteria));
        registrar("login", new PanelLoginCliente(boleteria));
        registrar("venta", new PanelVenta(ventas));
        registrar("devolucion", new PanelDevolucion(ventas));

        setJMenuBar(crearMenu());
        add(contenido, BorderLayout.CENTER);
        ir("inicio");
    }

    private void registrar(String id, Pantalla pantalla) {
        pantallas.put(id, pantalla);
        contenido.add((Component) pantalla, id);
    }

    /** Muestra la pantalla pedida; antes le avisa para que recargue sus datos. */
    public void ir(String id) {
        pantallas.get(id).alMostrar();
        tarjetas.show(contenido, id);
    }

    private JMenuBar crearMenu() {
        JMenu inicio = new JMenu("Inicio");
        inicio.add(item("Panel de control", KeyEvent.VK_I, () -> ir("inicio")));
        inicio.addSeparator();
        inicio.add(item("Salir", KeyEvent.VK_Q, () -> System.exit(0)));

        JMenu sedes = new JMenu("Sedes");
        sedes.add(item("Consulta", KeyEvent.VK_1, () -> ir("consulta")));
        sedes.add(item("Alta", KeyEvent.VK_2, () -> ir("alta")));
        sedes.add(item("Modificación", KeyEvent.VK_3, () -> ir("modificacion")));
        sedes.add(item("Baja", KeyEvent.VK_4, () -> ir("baja")));

        JMenu clientes = new JMenu("Clientes");
        clientes.add(item("Registro de cuenta", KeyEvent.VK_R, () -> ir("registro")));
        clientes.add(item("Iniciar sesión", KeyEvent.VK_L, () -> ir("login")));

        JMenu ventas = new JMenu("Ventas");
        ventas.add(item("Venta de entradas", KeyEvent.VK_V, () -> ir("venta")));
        ventas.add(item("Devoluciones", KeyEvent.VK_D, () -> ir("devolucion")));

        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem acercaDe = new JMenuItem("Acerca de...");
        acercaDe.addActionListener(new ActionListener() {   // clase anónima, la segunda forma de oyente
            @Override
            public void actionPerformed(ActionEvent e) {
                acercaDe();
            }
        });
        ayuda.add(acercaDe);

        JMenuBar barra = new JMenuBar();
        barra.add(inicio);
        barra.add(sedes);
        barra.add(clientes);
        barra.add(ventas);
        barra.add(ayuda);
        return barra;
    }

    /** Crea un ítem de menú con su acción (una lambda) y, si se indica una tecla, el atajo Ctrl+tecla. */
    private JMenuItem item(String texto, int tecla, Runnable accion) {
        JMenuItem item = new JMenuItem(texto);
        if (tecla != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(tecla, InputEvent.CTRL_DOWN_MASK));
        }
        item.addActionListener(e -> accion.run());
        return item;
    }

    private void acercaDe() {
        JOptionPane.showMessageDialog(this,
                "Boletería · Trabajo Práctico Obligatorio\nParadigma Orientado a Objetos · UADE 2026\n\n"
                        + "Grupo 3: Aded Fermín, Ávila Tomás, Vázquez Daniel, Villa Abrille Malena",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
}
