package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.*;

public class SelectorFecha extends JPanel {
    private LocalDate fecha = LocalDate.now().plusDays(1);
    private YearMonth mes = YearMonth.from(fecha);
    private JButton mostrar = new JButton();
    private JPopupMenu desplegable = new JPopupMenu();
    private JLabel titulo = new JLabel("", SwingConstants.CENTER);
    private JPanel dias = new JPanel(new GridLayout(6, 7, 3, 3));

    public SelectorFecha() {
        setName("fechaTurno"); setLayout(new BorderLayout());
        mostrar.setName("abrirCalendarioFecha");
        mostrar.setIcon(new IconoNavegacion("calendario", 22));
        mostrar.setIconTextGap(12); mostrar.setHorizontalAlignment(SwingConstants.LEFT);
        mostrar.setToolTipText("Elegir la fecha en el calendario");
        EstiloClinica.boton(mostrar, false, false);
        mostrar.setPreferredSize(new Dimension(220, 40));
        mostrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mes = YearMonth.from(fecha); dibujarMes();
                desplegable.show(mostrar, 0, mostrar.getHeight());
            }
        });
        add(mostrar, BorderLayout.CENTER);
        JPanel calendario = new JPanel(new BorderLayout(0, 10));
        calendario.setPreferredSize(new Dimension(340, 340));
        calendario.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel barra = new JPanel(new BorderLayout(8, 0));
        JButton anterior = boton("‹", "fechaMesAnterior");
        anterior.setToolTipText("Mes anterior");
        anterior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mes = mes.minusMonths(1); dibujarMes(); }
        });
        JButton siguiente = boton("›", "fechaMesSiguiente");
        siguiente.setToolTipText("Mes siguiente");
        siguiente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mes = mes.plusMonths(1); dibujarMes(); }
        });
        titulo.setName("fechaTituloMes"); titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        barra.add(anterior, BorderLayout.WEST); barra.add(titulo, BorderLayout.CENTER); barra.add(siguiente, BorderLayout.EAST);
        calendario.add(barra, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0, 5));
        JPanel semana = new JPanel(new GridLayout(1, 7));
        for (String nombre : new String[]{"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"}) {
            JLabel dia = new JLabel(nombre, SwingConstants.CENTER);
            dia.setFont(dia.getFont().deriveFont(12f)); semana.add(dia);
        }
        centro.add(semana, BorderLayout.NORTH); centro.add(dias, BorderLayout.CENTER);
        calendario.add(centro, BorderLayout.CENTER);
        JButton hoy = boton("Hoy", "fechaHoy");
        hoy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { elegir(LocalDate.now()); }
        });
        calendario.add(hoy, BorderLayout.SOUTH);
        desplegable.setName("miniCalendarioFecha"); desplegable.add(calendario);
        setFechaSeleccionada(fecha);
    }

    private JButton boton(String texto, String nombre) {
        JButton boton = new JButton(texto); boton.setName(nombre);
        EstiloClinica.boton(boton, false, false); return boton;
    }

    private void dibujarMes() {
        String nombre = mes.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-AR"));
        titulo.setText(Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1) + " " + mes.getYear());
        dias.removeAll();
        LocalDate inicio = mes.atDay(1).minusDays(mes.atDay(1).getDayOfWeek().getValue() - 1);
        for (int i = 0; i < 42; i++) {
            final LocalDate dia = inicio.plusDays(i);
            JButton boton = boton(Integer.toString(dia.getDayOfMonth()), "fechaDia-" + dia);
            boton.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
            boton.setFont(boton.getFont().deriveFont(Font.PLAIN, 13f));
            if (!YearMonth.from(dia).equals(mes)) { boton.setForeground(EstiloClinica.SECUNDARIO); }
            if (dia.equals(LocalDate.now())) { boton.setBackground(EstiloClinica.SUAVE); }
            if (dia.equals(fecha)) { boton.setBackground(EstiloClinica.PRIMARIO); boton.setForeground(Color.WHITE); }
            boton.setToolTipText(dia.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
            boton.getAccessibleContext().setAccessibleName("Elegir " + boton.getToolTipText());
            boton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { elegir(dia); }
            });
            dias.add(boton);
        }
        dias.revalidate(); dias.repaint();
    }

    private void elegir(LocalDate dia) {
        setFechaSeleccionada(dia); desplegable.setVisible(false); mostrar.requestFocusInWindow();
    }

    public LocalDate getFechaSeleccionada() { return fecha; }

    public void setFechaSeleccionada(LocalDate dia) {
        if (dia == null) { throw new IllegalArgumentException("Elegí una fecha."); }
        fecha = dia; mes = YearMonth.from(dia);
        mostrar.setText(dia.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
    }
}
