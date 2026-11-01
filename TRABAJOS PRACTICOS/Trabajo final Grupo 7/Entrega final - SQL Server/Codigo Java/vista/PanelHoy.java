package vista;

import aplicacion.AplicacionClinica;

import logica.SeguimientoTurnos;

import modelo.*;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelHoy extends PanelPantalla {
    private Clock reloj;
    private ArrayList<Turno> datos = new ArrayList<Turno>();
    private JButton hoy = new JButton("Hoy · …"),
            proximos = new JButton("Próximos 15 min · …"),
            vencidos = new JButton("Vencidos · …");
    private JButton refrescar;
    private JLabel estado = new JLabel("Consultando los turnos…");
    private DefaultTableModel filas =
            modelo(new String[] {"N°", "Hora", "Paciente", "Médico", "Consultorio", "Situación"});
    private JTable tabla = tabla(filas, "turnosHoy");
    private JScrollPane listado = new JScrollPane(tabla);
    private JLabel sinTurnos =
            new JLabel(
                    "Hoy no hay turnos. Consultá próximas fechas en Agenda o registrá uno desde Pedir turno.");
    private DefaultListModel<String> avisos = new DefaultListModel<String>();
    private JList<String> listaAvisos = new JList<String>(avisos);
    private ArrayList<Integer> idsAvisos = new ArrayList<Integer>();
    private HashSet<Integer> avisadosProximos = new HashSet<Integer>(),
            avisadosEnHorario = new HashSet<Integer>(),
            avisadosVencidos = new HashSet<Integer>();
    private javax.swing.Timer consulta, tiempo;
    private SwingWorker<ArrayList<Turno>, Void> tarea;
    private boolean cerrado, cargado, lecturaVigente, consultando, recargaPendiente;
    private LocalDateTime minuto;
    private Consumer<String> alResumen, alNotificacion;
    private JCheckBox avisosEscritorio = new JCheckBox("Avisos en escritorio", true);
    private JButton ausencias = new JButton("Ausencias / Vacaciones");
    private ArrayList<AusenciaMedica> periodos = new ArrayList<>();
    private HashSet<Integer> avisadosAusencias = new HashSet<>();

    public PanelHoy(AplicacionClinica app) {
        this(app, Clock.systemDefaultZone());
    }

    public PanelHoy(AplicacionClinica app, Clock reloj) {
        super(
                app,
                "Hoy",
                "Resumen de la jornada y avisos de los próximos turnos. La hora se toma de esta computadora.");
        this.reloj = reloj;
        hoy.setName("resumenHoy");
        proximos.setName("resumenProximos");
        vencidos.setName("resumenVencidos");
        for (JButton b : new JButton[] {hoy, proximos, vencidos}) {
            EstiloClinica.boton(b, false, false);
            b.setFont(b.getFont().deriveFont(Font.BOLD, 17f));
        }
        hoy.setToolTipText("Todos los turnos de hoy, incluidos atendidos y cancelados.");
        proximos.setToolTipText("Pendientes que comienzan dentro de 15 minutos.");
        vencidos.setToolTipText(
                "Pendientes cuya franja de 30 minutos ya terminó, de cualquier fecha.");
        hoy.addActionListener(e -> abrirAgenda("Hoy", null));
        proximos.addActionListener(e -> abrirAgenda("Próximos", null));
        vencidos.addActionListener(e -> abrirAgenda("Vencidos", null));
        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 16, 0));
        tarjetas.setOpaque(false);
        tarjetas.add(hoy);
        tarjetas.add(proximos);
        tarjetas.add(vencidos);
        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        centro.add(tarjetas, BorderLayout.NORTH);
        tabla.getColumnModel().getColumn(0).setMaxWidth(55);
        tabla.getColumnModel().getColumn(1).setMaxWidth(70);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(180);
        JTabbedPane vistas = new JTabbedPane();
        JPanel zonaHoy = new JPanel(new BorderLayout(0, 10));
        sinTurnos.setForeground(EstiloClinica.SECUNDARIO);
        sinTurnos.setBorder(BorderFactory.createEmptyBorder(10, 10, 4, 10));
        zonaHoy.add(sinTurnos, BorderLayout.NORTH);
        zonaHoy.add(listado, BorderLayout.CENTER);
        vistas.addTab("Turnos de hoy", zonaHoy);
        listaAvisos.setName("avisosTurnos");
        listaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAvisos.setFixedCellHeight(48);
        listaAvisos.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        vistas.addTab("Avisos de esta sesión", new JScrollPane(listaAvisos));
        centro.add(vistas, BorderLayout.CENTER);
        JPanel abajo = new JPanel(new BorderLayout(0, 8));
        abajo.setOpaque(false);
        ausencias.setName("alertaAusencias");
        EstiloClinica.boton(ausencias, false, false);
        ausencias.addActionListener(e -> PanelAusencias.abrir(this, app, null));
        refrescar = boton("Actualizar", "actualizarHoy", e -> actualizar());
        JButton ver =
                boton(
                        "Ver en Agenda",
                        "abrirTurnoHoy",
                        e -> {
                            if (vistas.getSelectedIndex() == 1
                                    && listaAvisos.getSelectedIndex() >= 0) {
                                int id = idsAvisos.get(listaAvisos.getSelectedIndex());
                                if (id == -2) PanelAusencias.abrir(this, app, null);
                                else abrirAgenda(id < 0 ? "Vencidos" : "Todos", id < 0 ? null : id);
                            } else if (tabla.getSelectedRow() >= 0)
                                abrirAgenda(
                                        "Todos",
                                        (Integer) filas.getValueAt(tabla.getSelectedRow(), 0));
                            else abrirAgenda("Hoy", null);
                        });
        avisosEscritorio.setName("activarAvisosEscritorio");
        avisosEscritorio.setOpaque(false);
        avisosEscritorio.setEnabled(SystemTray.isSupported());
        abajo.add(acciones(ver, refrescar, ausencias, avisosEscritorio), BorderLayout.NORTH);
        estado.setName("estadoActualizacionHoy");
        estado.setForeground(EstiloClinica.SECUNDARIO);
        abajo.add(estado, BorderLayout.CENTER);
        abajo.add(
                new JLabel(
                        "Los avisos funcionan mientras esta aplicación está abierta. Vencido no significa cancelado."),
                BorderLayout.SOUTH);
        centro.add(abajo, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
        consulta = new javax.swing.Timer(60000, e -> actualizar());
        tiempo =
                new javax.swing.Timer(
                        1000,
                        e -> {
                            LocalDateTime ahora =
                                    LocalDateTime.now(reloj).withSecond(0).withNano(0);
                            if (cargado && lecturaVigente && !ahora.equals(minuto)) {
                                minuto = ahora;
                                pintar(false);
                            }
                        });
        actualizar();
    }

    public void setAlResumen(Consumer<String> accion) {
        alResumen = accion;
    }

    public void setAlNotificacion(Consumer<String> accion) {
        alNotificacion = accion;
    }

    private void abrirAgenda(String filtro, Integer id) {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof VentanaPrincipal) ((VentanaPrincipal) w).abrirAgendaResumen(filtro, id);
    }

    public void actualizar() {
        if (cerrado) return;
        if (consultando) {
            recargaPendiente = true;
            return;
        }
        consultando = true;
        refrescar.setEnabled(false);
        tarea =
                new SwingWorker<ArrayList<Turno>, Void>() {
                    private ArrayList<AusenciaMedica> leidas;

                    protected ArrayList<Turno> doInBackground() {
                        leidas = new logica.RegistroAusencias().listar();
                        return app.getGestor().consultarTurnos();
                    }

                    protected void done() {
                        if (cerrado || isCancelled()) return;
                        try {
                            datos = get();
                            periodos = leidas;
                            datos.sort(
                                    Comparator.comparing(Turno::getFecha)
                                            .thenComparing(Turno::getHora)
                                            .thenComparingInt(Turno::getId));
                            boolean primera = !cargado;
                            cargado = true;
                            lecturaVigente = true;
                            pintar(primera);
                            estado.setText(
                                    "Actualizado "
                                            + LocalTime.now(reloj)
                                                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                                            + " · Consulta automática cada minuto");
                        } catch (Exception error) {
                            lecturaVigente = false;
                            estado.setText(
                                    "No se pudo actualizar. Revisá la conexión SQL; los datos visibles son de la última lectura.");
                            if (alResumen != null)
                                alResumen.accept("Avisos sin actualizar: revisar conexión SQL.");
                        } finally {
                            consultando = false;
                            refrescar.setEnabled(true);
                            if (recargaPendiente) {
                                recargaPendiente = false;
                                actualizar();
                            }
                        }
                    }
                };
        tarea.execute();
    }

    private void pintar(boolean primera) {
        LocalDateTime ahora = LocalDateTime.now(reloj);
        int totalHoy = 0, totalProximos = 0, totalVencidos = 0;
        ArrayList<Turno> afectados = logica.RegistroAusencias.afectados(datos, periodos, ahora);
        HashSet<Integer> idsAfectados = new HashSet<>();
        for (Turno t : afectados) idsAfectados.add(t.getId());
        avisadosAusencias.retainAll(idsAfectados);
        ausencias.setText(
                afectados.isEmpty()
                        ? "Ausencias / Vacaciones"
                        : "Reasignar por ausencia · " + afectados.size());
        ausencias.setBackground(
                afectados.isEmpty() ? EstiloClinica.SUAVE : new Color(255, 235, 204));
        int nuevos = 0;
        for (Turno t : afectados) if (avisadosAusencias.add(t.getId())) nuevos++;
        if (nuevos > 0)
            agregarAviso(
                    nuevos
                            + " turno(s) afectados por ausencia médica. Abrí Ausencias / Vacaciones para reasignarlos.",
                    -2);
        Integer elegido =
                tabla.getSelectedRow() < 0
                        ? null
                        : (Integer) filas.getValueAt(tabla.getSelectedRow(), 0);
        Point posicion = listado.getViewport().getViewPosition();
        filas.setRowCount(0);
        Turno siguiente = null;
        for (Turno t : datos) {
            boolean requiereCambio = idsAfectados.contains(t.getId());
            String situacion =
                    requiereCambio
                            ? "Reasignar por ausencia"
                            : SeguimientoTurnos.situacion(t, ahora);
            if (t.getFecha().equals(ahora.toLocalDate())) {
                totalHoy++;
                filas.addRow(
                        new Object[] {
                            t.getId(),
                            t.getHora(),
                            t.getPaciente().getNombre(),
                            t.getMedico().getNombre(),
                            t.getMedico().getConsultorio(),
                            situacion
                        });
                if (elegido != null && elegido == t.getId())
                    tabla.setRowSelectionInterval(filas.getRowCount() - 1, filas.getRowCount() - 1);
            }
            if (!requiereCambio
                    && t.getEstado() == EstadoTurno.PENDIENTE
                    && !LocalDateTime.of(t.getFecha(), t.getHora()).isBefore(ahora)
                    && siguiente == null) siguiente = t;
            if (!requiereCambio && SeguimientoTurnos.proximo(t, ahora)) {
                totalProximos++;
                if (avisadosProximos.add(t.getId())) avisar("Próximo turno", t);
            }
            if (!requiereCambio
                    && SeguimientoTurnos.enHorario(t, ahora)
                    && avisadosEnHorario.add(t.getId())) avisar("Turno en horario", t);
            if (SeguimientoTurnos.vencido(t, ahora)) {
                totalVencidos++;
                if (avisadosVencidos.add(t.getId()) && !primera) avisar("Vencido sin registrar", t);
            }
        }
        if (primera && totalVencidos > 0)
            agregarAviso(
                    totalVencidos
                            + " turno(s) vencidos siguen pendientes. Revisá si fueron atendidos o cancelados.",
                    -1);
        sinTurnos.setVisible(filas.getRowCount() == 0);
        listado.getViewport().setViewPosition(posicion);
        hoy.setText("Hoy · " + totalHoy);
        proximos.setText("Próximos 15 min · " + totalProximos);
        vencidos.setText("Vencidos · " + totalVencidos);
        String resumen =
                "Hoy: "
                        + totalHoy
                        + " turno(s) · "
                        + totalProximos
                        + " próximo(s) en 15 min · "
                        + totalVencidos
                        + " vencido(s) sin registrar · "
                        + afectados.size()
                        + " a reasignar por ausencia";
        if (siguiente != null)
            resumen +=
                    " · Próximo: "
                            + siguiente.getFecha().format(DateTimeFormatter.ofPattern("dd/MM"))
                            + " "
                            + siguiente.getHora();
        if (alResumen != null) alResumen.accept(resumen);
    }

    private void avisar(String titulo, Turno t) {
        agregarAviso(
                titulo
                        + " · "
                        + t.getHora()
                        + " · "
                        + t.getPaciente().getNombre()
                        + " con "
                        + t.getMedico().getNombre()
                        + " · "
                        + t.getMedico().getConsultorio(),
                t.getId());
    }

    private void agregarAviso(String texto, int id) {
        avisos.add(
                0,
                LocalTime.now(reloj).format(DateTimeFormatter.ofPattern("HH:mm")) + "  " + texto);
        idsAvisos.add(0, id);
        if (avisos.size() > 30) {
            avisos.remove(30);
            idsAvisos.remove(30);
        }
        if (avisosEscritorio.isSelected() && alNotificacion != null) alNotificacion.accept(texto);
    }

    public void addNotify() {
        super.addNotify();
        if (consulta != null && !cerrado) {
            consulta.start();
            tiempo.start();
        }
    }

    public void removeNotify() {
        if (consulta != null) {
            consulta.stop();
            tiempo.stop();
        }
        super.removeNotify();
    }

    public void detener() {
        cerrado = true;
        consulta.stop();
        tiempo.stop();
        if (tarea != null) tarea.cancel(true);
    }
}
