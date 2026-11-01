package vista;

import aplicacion.AplicacionClinica;

import logica.SeguimientoTurnos;
import logica.TurnoException;

import modelo.EstadoTurno;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelAgenda extends PanelPantalla {
    private DefaultTableModel modelo =
            modelo(
                    new String[] {
                        "Numero", "Fecha", "Hora", "Paciente", "Medico", "Estado", "Situación"
                    });
    private JTable tabla = tabla(modelo, "tablaTurnos");
    private JComboBox<Object> medicos = new JComboBox<Object>();
    private JComboBox<Object> pacientes = new JComboBox<Object>();
    private JComboBox<String> estados =
            new JComboBox<String>(
                    new String[] {
                        "Todos",
                        "PENDIENTE",
                        "ATENDIDO",
                        "CANCELADO",
                        "Vencidos",
                        "Próximos",
                        "En horario"
                    });
    private JTextField fecha = campo("fechaFiltro");
    private JLabel resultado = new JLabel();
    private JButton cancelar;
    private JButton atender;
    private JButton correo;
    private JButton comprobante;
    private PanelCalendario calendario;
    private JPanel vistas = new JPanel(new CardLayout());
    private JPanel bloqueFiltros = new JPanel(new BorderLayout());
    private JPanel zonaAcciones;
    private boolean vistaCalendario = true;
    private JButton botonCalendario;
    private JButton botonLista;
    private javax.swing.Timer timerSituacion;

    public PanelAgenda(AplicacionClinica app) {
        super(
                app,
                "Agenda",
                "Usá las flechas para cambiar de mes o elegí un día para ver sus turnos.");
        medicos.setName("medicoFiltro");
        pacientes.setName("pacienteFiltro");
        estados.setName("estadoFiltro");
        EstiloClinica.combo(medicos);
        EstiloClinica.combo(pacientes);
        EstiloClinica.combo(estados);
        tabla.getColumnModel().getColumn(0).setMaxWidth(75);
        tabla.getColumnModel().getColumn(0).setMinWidth(70);
        tabla.getColumnModel().getColumn(1).setMinWidth(110);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(1).setMaxWidth(130);
        tabla.getColumnModel().getColumn(2).setMinWidth(65);
        tabla.getColumnModel().getColumn(2).setMaxWidth(75);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(240);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(240);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(5).setMinWidth(105);
        JPanel filtros = new JPanel(new GridLayout(2, 4, 10, 8));
        filtros.add(new JLabel("Medico"));
        filtros.add(new JLabel("Paciente"));
        filtros.add(new JLabel("Fecha · año-mes-día"));
        filtros.add(new JLabel("Estado"));
        fecha.setToolTipText("Opcional. Ejemplo: " + LocalDate.now().plusDays(1));
        filtros.add(medicos);
        filtros.add(pacientes);
        filtros.add(fecha);
        filtros.add(estados);
        JPanel centro = new JPanel(new BorderLayout(8, 12));
        bloqueFiltros.add(filtros, BorderLayout.CENTER);
        bloqueFiltros.add(
                acciones(
                        boton(
                                "Buscar",
                                "buscarTurnos",
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        filtrar();
                                    }
                                }),
                        boton(
                                "Limpiar filtros",
                                "todosTurnos",
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        medicos.setSelectedIndex(0);
                                        pacientes.setSelectedIndex(0);
                                        fecha.setText("");
                                        estados.setSelectedIndex(0);
                                        filtrar();
                                    }
                                })),
                BorderLayout.SOUTH);
        calendario =
                new PanelCalendario(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                fecha.setText(e.getActionCommand());
                                mostrarVista(false);
                                filtrar();
                            }
                        });
        calendario.setName("calendarioTurnos");
        vistas.add(calendario, "calendario");
        vistas.add(new JScrollPane(tabla), "lista");
        JPanel arriba = new JPanel(new BorderLayout(0, 8));
        arriba.setOpaque(false);
        botonCalendario =
                boton(
                        "Calendario",
                        "vistaCalendario",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                fecha.setText("");
                                mostrarVista(true);
                                filtrar();
                            }
                        });
        botonLista =
                boton(
                        "Lista",
                        "vistaLista",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                mostrarVista(false);
                                filtrar();
                            }
                        });
        arriba.add(
                acciones(
                        botonCalendario,
                        botonLista,
                        boton(
                                "Filtros",
                                "mostrarFiltros",
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        bloqueFiltros.setVisible(!bloqueFiltros.isVisible());
                                        revalidate();
                                    }
                                }),
                        boton(
                                "Pedir turno",
                                "nuevoDesdeAgenda",
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        nuevoTurno();
                                    }
                                })),
                BorderLayout.NORTH);
        arriba.add(bloqueFiltros, BorderLayout.CENTER);
        bloqueFiltros.setVisible(false);
        centro.add(arriba, BorderLayout.NORTH);
        centro.add(vistas, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        cancelar =
                boton(
                        "Cancelar turno",
                        "cancelarTurno",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                cambiarEstado(false);
                            }
                        });
        atender =
                boton(
                        "Marcar atendido",
                        "atenderTurno",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                cambiarEstado(true);
                            }
                        });
        correo =
                boton(
                        "Ver recordatorio",
                        "recordatorioTurno",
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                mostrarCorreo();
                            }
                        });
        JPanel abajo = new JPanel(new BorderLayout());
        comprobante =
                boton(
                        "Comprobante PDF",
                        "comprobanteTurno",
                        e -> {
                            try {
                                Turno turno = app.getGestor().consultarTurno(numeroSeleccionado());
                                new ConfirmacionTurno(SwingUtilities.getWindowAncestor(this), turno)
                                        .setVisible(true);
                            } catch (TurnoException | IllegalArgumentException error) {
                                error(error);
                            }
                        });
        zonaAcciones = acciones(cancelar, atender, correo, comprobante);
        abajo.add(zonaAcciones, BorderLayout.CENTER);
        zonaAcciones.setVisible(false);
        abajo.add(resultado, BorderLayout.SOUTH);
        add(abajo, BorderLayout.SOUTH);
        tabla.getSelectionModel()
                .addListSelectionListener(
                        new ListSelectionListener() {
                            public void valueChanged(ListSelectionEvent e) {
                                actualizarBotones();
                            }
                        });
        tabla.getColumnModel().getColumn(6).setPreferredWidth(180);
        timerSituacion = new javax.swing.Timer(1000, e -> actualizarSituaciones());
        mostrarVista(true);
        actualizar();
    }

    public void addNotify() {
        super.addNotify();
        if (timerSituacion != null) timerSituacion.start();
    }

    public void removeNotify() {
        if (timerSituacion != null) timerSituacion.stop();
        super.removeNotify();
    }

    private void actualizarSituaciones() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String valor =
                    SeguimientoTurnos.situacion(
                            (EstadoTurno) modelo.getValueAt(i, 5),
                            (LocalDate) modelo.getValueAt(i, 1),
                            (java.time.LocalTime) modelo.getValueAt(i, 2),
                            ahora);
            if (!valor.equals(modelo.getValueAt(i, 6))) modelo.setValueAt(valor, i, 6);
        }
    }

    public void mostrarResumen(String filtro, Integer id) {
        medicos.setSelectedIndex(0);
        pacientes.setSelectedIndex(0);
        estados.setSelectedItem("Todos");
        fecha.setText("");
        if ("Hoy".equals(filtro)) fecha.setText(LocalDate.now().toString());
        else if (!"Todos".equals(filtro)) estados.setSelectedItem(filtro);
        mostrarVista(false);
        filtrar();
        if (id != null)
            for (int i = 0; i < modelo.getRowCount(); i++)
                if (((Integer) modelo.getValueAt(i, 0)).equals(id)) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                    break;
                }
    }

    private void mostrarVista(boolean mensual) {
        vistaCalendario = mensual;
        ((CardLayout) vistas.getLayout()).show(vistas, mensual ? "calendario" : "lista");
        bloqueFiltros.setVisible(!mensual);
        zonaAcciones.setVisible(!mensual);
        botonCalendario.setBackground(mensual ? EstiloClinica.PRIMARIO : java.awt.Color.WHITE);
        botonCalendario.setForeground(mensual ? java.awt.Color.WHITE : EstiloClinica.TEXTO);
        botonLista.setBackground(mensual ? java.awt.Color.WHITE : EstiloClinica.PRIMARIO);
        botonLista.setForeground(mensual ? EstiloClinica.TEXTO : java.awt.Color.WHITE);
        revalidate();
        repaint();
    }

    private void nuevoTurno() {
        try {
            LocalDate dia =
                    fecha.getText().trim().isEmpty()
                            ? calendario.getMesVisible().equals(YearMonth.now())
                                    ? LocalDate.now().plusDays(1)
                                    : calendario.getMesVisible().atDay(1)
                            : LocalDate.parse(fecha.getText().trim());
            VentanaPrincipal ventana = (VentanaPrincipal) SwingUtilities.getWindowAncestor(this);
            ventana.abrirNuevoTurno(dia);
        } catch (DateTimeParseException e) {
            error(new IllegalArgumentException("Revisá la fecha antes de reservar."));
        }
    }

    private int numeroSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            throw new IllegalArgumentException("Seleccionar un turno de la tabla.");
        }
        return (Integer) modelo.getValueAt(fila, 0);
    }

    private void actualizarBotones() {
        int fila = tabla.getSelectedRow();
        boolean pendiente = fila != -1 && modelo.getValueAt(fila, 5).equals(EstadoTurno.PENDIENTE);
        cancelar.setEnabled(pendiente);
        atender.setEnabled(pendiente);
        correo.setEnabled(pendiente);
        comprobante.setEnabled(fila != -1);
    }

    private void cambiarEstado(boolean registrarAtencion) {
        try {
            int numero = numeroSeleccionado();
            int fila = tabla.getSelectedRow();
            String pregunta =
                    registrarAtencion
                            ? "¿Marcar este turno como atendido?"
                            : "¿Cancelar este turno?";
            String detalle =
                    "\n\nPaciente: "
                            + modelo.getValueAt(fila, 3)
                            + "\nMédico: "
                            + modelo.getValueAt(fila, 4)
                            + "\nFecha: "
                            + modelo.getValueAt(fila, 1)
                            + "   Hora: "
                            + modelo.getValueAt(fila, 2);
            if (!confirmar(pregunta + detalle)) {
                return;
            }
            if (registrarAtencion) {
                app.getGestor().atenderTurno(numero);
            } else {
                app.getGestor().cancelarTurno(numero);
            }
            java.awt.Window ventana = SwingUtilities.getWindowAncestor(this);
            if (ventana instanceof VentanaPrincipal)
                ((VentanaPrincipal) ventana).actualizarResumen();
            filtrar();
            informar(
                    registrarAtencion
                            ? "Atencion registrada."
                            : "Turno cancelado. El horario queda libre.");
        } catch (TurnoException e) {
            error(e);
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    private void mostrarCorreo() {
        try {
            app.getGestor().enviarRecordatorio(numeroSeleccionado());
            JTextArea mensaje = new JTextArea(app.getAvisoCorreo(), 12, 54);
            mensaje.setEditable(false);
            mensaje.setLineWrap(true);
            mensaje.setWrapStyleWord(true);
            mensaje.setCaretPosition(0);
            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(mensaje),
                    "Recordatorio simulado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (TurnoException e) {
            error(e);
        } catch (IllegalArgumentException e) {
            error(e);
        }
    }

    private void filtrar() {
        try {
            LocalDate fechaElegida =
                    fecha.getText().trim().isEmpty()
                            ? null
                            : LocalDate.parse(fecha.getText().trim());
            Object medico = medicos.getSelectedItem();
            Object paciente = pacientes.getSelectedItem();
            Object estado = estados.getSelectedItem();
            ArrayList<Turno> turnosCalendario = new ArrayList<Turno>();
            Integer anterior =
                    tabla.getSelectedRow() < 0
                            ? null
                            : (Integer) modelo.getValueAt(tabla.getSelectedRow(), 0);
            tabla.clearSelection();
            modelo.setRowCount(0);
            java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
            for (Turno turno : app.getGestor().consultarTurnos()) {
                if (medico instanceof Medico
                        && !turno.getMedico()
                                .getMatricula()
                                .equals(((Medico) medico).getMatricula())) {
                    continue;
                }
                if (paciente instanceof Paciente
                        && !turno.getPaciente().getDni().equals(((Paciente) paciente).getDni())) {
                    continue;
                }
                if ("Vencidos".equals(estado)) {
                    if (!SeguimientoTurnos.vencido(turno, ahora)) continue;
                } else if ("Próximos".equals(estado)) {
                    if (!SeguimientoTurnos.proximo(turno, ahora)) continue;
                } else if ("En horario".equals(estado)) {
                    if (!SeguimientoTurnos.enHorario(turno, ahora)) continue;
                } else if (!"Todos".equals(estado) && !turno.getEstado().name().equals(estado))
                    continue;
                turnosCalendario.add(turno);
                if (fechaElegida != null && !turno.getFecha().equals(fechaElegida)) {
                    continue;
                }
                modelo.addRow(
                        new Object[] {
                            turno.getId(),
                            turno.getFecha(),
                            turno.getHora(),
                            turno.getPaciente().getNombre(),
                            turno.getMedico().getNombre(),
                            turno.getEstado(),
                            SeguimientoTurnos.situacion(turno, ahora)
                        });
                if (anterior != null && anterior == turno.getId())
                    tabla.setRowSelectionInterval(
                            modelo.getRowCount() - 1, modelo.getRowCount() - 1);
            }
            if (fechaElegida != null) {
                calendario.mostrarMes(YearMonth.from(fechaElegida));
            }
            calendario.actualizarTurnos(turnosCalendario);
            boolean sinFiltros =
                    medicos.getSelectedIndex() == 0
                            && pacientes.getSelectedIndex() == 0
                            && fechaElegida == null
                            && estados.getSelectedIndex() == 0;
            resultado.setText(
                    modelo.getRowCount() == 0
                            ? sinFiltros
                                    ? "Todavía no hay turnos. Registrá el primero desde Pedir turno."
                                    : "No encontramos turnos con estos filtros. Probá Limpiar filtros."
                            : "Turnos encontrados: "
                                    + modelo.getRowCount()
                                    + " · Seleccioná una fila para continuar.");
            if (vistaCalendario) {
                resultado.setText(
                        "Elegí un día para consultar sus turnos o reservar. Verde: pendiente o atendido · Rojo: cancelado.");
            }
            actualizarBotones();
        } catch (DateTimeParseException e) {
            error(
                    new IllegalArgumentException(
                            "La fecha del filtro debe tener el formato aaaa-mm-dd."));
        }
    }

    public void actualizar() {
        Object medico = medicos.getSelectedItem();
        Object paciente = pacientes.getSelectedItem();
        medicos.removeAllItems();
        pacientes.removeAllItems();
        medicos.addItem("Todos");
        pacientes.addItem("Todos");
        for (Medico actual : app.getMedicos().listar()) {
            medicos.addItem(actual);
            if (medico instanceof Medico
                    && actual.getMatricula().equals(((Medico) medico).getMatricula())) {
                medicos.setSelectedItem(actual);
            }
        }
        for (Paciente actual : app.getPacientes().listar()) {
            pacientes.addItem(actual);
            if (paciente instanceof Paciente
                    && actual.getDni().equals(((Paciente) paciente).getDni())) {
                pacientes.setSelectedItem(actual);
            }
        }
        filtrar();
    }
}
