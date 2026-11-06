package vista;

import modelo.EstadoTurno;
import modelo.Turno;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import javax.swing.*;

public class PanelCalendario extends JPanel {
    private YearMonth mes = YearMonth.now();
    private JLabel tituloMes = new JLabel();
    private JPanel dias = new JPanel();
    private ArrayList<Turno> turnos = new ArrayList<Turno>();
    private ActionListener seleccionarDia;

    public PanelCalendario(ActionListener seleccionarDia) {
        this.seleccionarDia = seleccionarDia;
        setLayout(new BorderLayout(0, 10)); setOpaque(false);
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        barra.setOpaque(false);
        JButton hoy = nuevoBoton("Hoy", "calendarioHoy");
        hoy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarMes(YearMonth.now()); }
        });
        JButton anterior = nuevoBoton("‹", "mesAnterior");
        anterior.setToolTipText("Mes anterior");
        anterior.getAccessibleContext().setAccessibleName("Mes anterior");
        anterior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarMes(mes.minusMonths(1)); }
        });
        JButton siguiente = nuevoBoton("›", "mesSiguiente");
        siguiente.setToolTipText("Mes siguiente");
        siguiente.getAccessibleContext().setAccessibleName("Mes siguiente");
        siguiente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarMes(mes.plusMonths(1)); }
        });
        tituloMes.setName("tituloMes");
        tituloMes.setFont(tituloMes.getFont().deriveFont(Font.BOLD, 23f));
        barra.add(hoy); barra.add(anterior); barra.add(siguiente); barra.add(tituloMes);
        add(barra, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 0));
        JPanel semana = new JPanel(new GridLayout(1, 7));
        for (String nombre : new String[]{"LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM"}) {
            JLabel etiqueta = new JLabel(nombre, SwingConstants.CENTER);
            etiqueta.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 12f));
            etiqueta.setForeground(EstiloClinica.SECUNDARIO); semana.add(etiqueta);
        }
        cuerpo.add(semana, BorderLayout.NORTH); cuerpo.add(dias, BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER); dibujarMes();
    }

    private JButton nuevoBoton(String texto, String nombre) {
        JButton boton = new JButton(texto); boton.setName(nombre);
        EstiloClinica.boton(boton, false, false); return boton;
    }

    public YearMonth getMesVisible() { return mes; }

    public void mostrarMes(YearMonth nuevoMes) {
        mes = nuevoMes; dibujarMes();
    }

    public void actualizarTurnos(ArrayList<Turno> nuevosTurnos) {
        turnos = new ArrayList<Turno>(nuevosTurnos);
        // En cada dia se leen primero los turnos mas tempranos.
        Collections.sort(turnos, new Comparator<Turno>() {
            public int compare(Turno a, Turno b) {
                int porFecha = a.getFecha().compareTo(b.getFecha());
                return porFecha != 0 ? porFecha : a.getHora().compareTo(b.getHora());
            }
        });
        dibujarMes();
    }

    private void dibujarMes() {
        String nombre = mes.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-AR"));
        tituloMes.setText(Character.toUpperCase(nombre.charAt(0)) + nombre.substring(1) + " de " + mes.getYear());
        dias.removeAll();
        // La semana empieza el lunes; tambien se muestran los dias que completan la fila.
        int anteriores = mes.atDay(1).getDayOfWeek().getValue() - 1;
        int filas = (anteriores + mes.lengthOfMonth() + 6) / 7;
        dias.setLayout(new GridLayout(filas, 7, 1, 1)); dias.setBackground(EstiloClinica.BORDE);
        dias.setBorder(BorderFactory.createLineBorder(EstiloClinica.BORDE));
        LocalDate inicio = mes.atDay(1).minusDays(anteriores);
        for (int i = 0; i < filas * 7; i++) {
            LocalDate fecha = inicio.plusDays(i);
            dias.add(crearDia(fecha));
        }
        dias.revalidate(); dias.repaint();
    }

    private JButton crearDia(final LocalDate fecha) {
        boolean esteMes = YearMonth.from(fecha).equals(mes);
        boolean hoy = fecha.equals(LocalDate.now());
        String color = esteMes ? "#17324D" : "#697987";
        String numero = Integer.toString(fecha.getDayOfMonth());
        if (fecha.getDayOfMonth() == 1) {
            numero += " " + fecha.getMonth().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-AR"));
        }
        String texto = "<html><font color='" + (hoy ? "#0F766E" : color) + "'><b>"
                + numero + (hoy ? " · Hoy" : "") + "</b></font>";
        String detalle = "<html>" + fecha;
        int cantidad = 0;
        final ArrayList<Turno> turnosDelDia = new ArrayList<Turno>();
        for (Turno turno : turnos) {
            if (!turno.getFecha().equals(fecha)) { continue; }
            turnosDelDia.add(turno);
            cantidad++;
            String persona = turno.getPaciente().getNombre();
            String abreviado = persona.length() > 12 ? persona.substring(0, 11) + "…" : persona;
            String estado = turno.getEstado() == EstadoTurno.CANCELADO ? "#B91C1C"
                    : turno.getEstado() == EstadoTurno.ATENDIDO ? "#166534" : "#0F766E";
            if (cantidad <= 2) {
                texto += "<br><font size='3' color='" + estado + "'>" + turno.getHora()
                        + " " + escapar(abreviado) + "</font>";
            }
            detalle += "<br>" + turno.getHora() + " · " + escapar(persona)
                    + " · " + escapar(turno.getMedico().getNombre())
                    + " · " + EstiloClinica.nombreEstado(turno.getEstado());
        }
        if (cantidad > 2) { texto += "<br><font size='3'>+ " + (cantidad - 2) + " más</font>"; }
        texto += "</html>";
        detalle += cantidad == 0 ? "<br>Sin turnos. Clic para consultar o reservar.</html>" : "</html>";
        final String tituloDia = numero;
        final int total = cantidad;
        JButton boton = new JButton(texto) {
            protected void paintComponent(Graphics grafico) {
                // Se adapta al alto de la celda: el total siempre se puede leer.
                Graphics2D g = (Graphics2D) grafico.create();
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setColor(getBackground()); g.fillRect(0, 0, getWidth(), getHeight());
                g.setFont(getFont().deriveFont(Font.BOLD, 12f));
                g.setColor(hoy ? EstiloClinica.PRIMARIO : esteMes ? EstiloClinica.TEXTO : EstiloClinica.SECUNDARIO);
                g.drawString(recortar(g, tituloDia + (total > 0 ? " · " + total + " turnos" : ""), getWidth() - 12), 6, 17);
                g.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                int visibles = Math.min(2, Math.max(0, (getHeight() - 24) / 17));
                for (int i = 0; i < Math.min(visibles, total); i++) {
                    Turno turno = turnosDelDia.get(i);
                    g.setColor(turno.getEstado() == EstadoTurno.CANCELADO ? EstiloClinica.PELIGRO
                            : turno.getEstado() == EstadoTurno.ATENDIDO ? Color.decode("#166534") : EstiloClinica.PRIMARIO);
                    int y = 35 + i * 17;
                    g.fillOval(6, y - 7, 5, 5);
                    g.drawString(recortar(g, turno.getHora() + " " + turno.getPaciente().getNombre(), getWidth() - 23), 16, y);
                }
                if (hasFocus()) {
                    g.setColor(EstiloClinica.PRIMARIO); g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
                }
                g.dispose();
            }
        };
        boton.setName("dia-" + fecha); boton.setActionCommand(fecha.toString());
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            protected void paintFocus(Graphics g, AbstractButton b, Rectangle vista, Rectangle texto, Rectangle icono) {
                g.setColor(EstiloClinica.PRIMARIO); g.drawRect(2, 2, b.getWidth() - 5, b.getHeight() - 5);
            }
        });
        boton.setHorizontalAlignment(SwingConstants.LEFT); boton.setVerticalAlignment(SwingConstants.TOP);
        boton.setMargin(new Insets(8, 7, 6, 5));
        boton.setBackground(hoy ? EstiloClinica.SUAVE : esteMes ? Color.WHITE : new Color(245, 248, 249));
        boton.setOpaque(true); boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setToolTipText(detalle); boton.getAccessibleContext().setAccessibleName(fecha + ", " + cantidad + " turnos");
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(seleccionarDia);
        return boton;
    }

    private String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String recortar(Graphics2D g, String texto, int ancho) {
        if (g.getFontMetrics().stringWidth(texto) <= ancho) { return texto; }
        while (!texto.isEmpty() && g.getFontMetrics().stringWidth(texto + "…") > ancho) {
            texto = texto.substring(0, texto.length() - 1);
        }
        return texto + "…";
    }
}
