package vista;

import aplicacion.AplicacionClinica;
import logica.TurnoException;
import modelo.EstadoTurno;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PanelAgenda extends PanelPantalla {
    private DefaultTableModel modelo = modelo(new String[]{"Numero", "Fecha", "Hora", "Paciente", "Medico", "Estado"});
    private JTable tabla = tabla(modelo, "tablaTurnos");
    private JComboBox<Object> medicos = new JComboBox<Object>();
    private JComboBox<Object> pacientes = new JComboBox<Object>();
    private JComboBox<String> estados = new JComboBox<String>(new String[]{"Todos", "PENDIENTE", "ATENDIDO", "CANCELADO"});
    private JTextField fecha = campo("fechaFiltro");
    private JLabel resultado = new JLabel();
    private JButton cancelar;
    private JButton atender;
    private JButton correo;
    private PanelCalendario calendario;
    private JPanel vistas = new JPanel(new CardLayout());
    private JPanel bloqueFiltros = new JPanel(new BorderLayout());
    private JPanel zonaAcciones;
    private boolean vistaCalendario = true;
    private JButton botonCalendario;
    private JButton botonLista;

    public PanelAgenda(AplicacionClinica app) {
        super(app, "Agenda", "Usá las flechas para cambiar de mes o elegí un día para ver sus turnos.");
        medicos.setName("medicoFiltro"); pacientes.setName("pacienteFiltro"); estados.setName("estadoFiltro");
        EstiloClinica.combo(medicos); EstiloClinica.combo(pacientes); EstiloClinica.combo(estados);
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
        filtros.add(new JLabel("Medico")); filtros.add(new JLabel("Paciente"));
        filtros.add(new JLabel("Fecha · año-mes-día")); filtros.add(new JLabel("Estado"));
        fecha.setToolTipText("Opcional. Ejemplo: " + LocalDate.now().plusDays(1));
        filtros.add(medicos); filtros.add(pacientes); filtros.add(fecha); filtros.add(estados);
        JPanel centro = new JPanel(new BorderLayout(8, 12));
        bloqueFiltros.add(filtros, BorderLayout.CENTER);
        bloqueFiltros.add(acciones(boton("Buscar", "buscarTurnos", new ActionListener() {
            public void actionPerformed(ActionEvent e) { filtrar(); }
        }), boton("Limpiar filtros", "todosTurnos", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                medicos.setSelectedIndex(0); pacientes.setSelectedIndex(0);
                fecha.setText(""); estados.setSelectedIndex(0); filtrar();
            }
        })), BorderLayout.SOUTH);
        calendario = new PanelCalendario(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fecha.setText(e.getActionCommand());
                mostrarVista(false); filtrar();
            }
        });
        calendario.setName("calendarioTurnos");
        vistas.add(calendario, "calendario"); vistas.add(new JScrollPane(tabla), "lista");
        JPanel arriba = new JPanel(new BorderLayout(0, 8));
        arriba.setOpaque(false);
        botonCalendario = boton("Calendario", "vistaCalendario", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fecha.setText(""); mostrarVista(true); filtrar();
            }
        });
        botonLista = boton("Lista", "vistaLista", new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarVista(false); filtrar(); }
        });
        arriba.add(acciones(botonCalendario, botonLista, boton("Filtros", "mostrarFiltros", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bloqueFiltros.setVisible(!bloqueFiltros.isVisible()); revalidate();
            }
        }), boton("Pedir turno", "nuevoDesdeAgenda", new ActionListener() {
            public void actionPerformed(ActionEvent e) { nuevoTurno(); }
        })), BorderLayout.NORTH);
        arriba.add(bloqueFiltros, BorderLayout.CENTER); bloqueFiltros.setVisible(false);
        centro.add(arriba, BorderLayout.NORTH); centro.add(vistas, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        cancelar = boton("Cancelar turno", "cancelarTurno", new ActionListener() {
            public void actionPerformed(ActionEvent e) { cambiarEstado(false); }
        });
        atender = boton("Marcar atendido", "atenderTurno", new ActionListener() {
            public void actionPerformed(ActionEvent e) { cambiarEstado(true); }
        });
        correo = boton("Ver recordatorio", "recordatorioTurno", new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarCorreo(); }
        });
        JPanel abajo = new JPanel(new BorderLayout());
        zonaAcciones = acciones(cancelar, atender, correo);
        abajo.add(zonaAcciones, BorderLayout.CENTER); zonaAcciones.setVisible(false);
        abajo.add(resultado, BorderLayout.SOUTH); add(abajo, BorderLayout.SOUTH);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) { actualizarBotones(); }
        });
        mostrarVista(true); actualizar();
    }

    private void mostrarVista(boolean mensual) {
        vistaCalendario = mensual;
        ((CardLayout) vistas.getLayout()).show(vistas, mensual ? "calendario" : "lista");
        bloqueFiltros.setVisible(!mensual); zonaAcciones.setVisible(!mensual);
        botonCalendario.setBackground(mensual ? EstiloClinica.PRIMARIO : java.awt.Color.WHITE);
        botonCalendario.setForeground(mensual ? java.awt.Color.WHITE : EstiloClinica.TEXTO);
        botonLista.setBackground(mensual ? java.awt.Color.WHITE : EstiloClinica.PRIMARIO);
        botonLista.setForeground(mensual ? EstiloClinica.TEXTO : java.awt.Color.WHITE);
        revalidate(); repaint();
    }

    private void nuevoTurno() {
        try {
            LocalDate dia = fecha.getText().trim().isEmpty()
                    ? calendario.getMesVisible().equals(YearMonth.now()) ? LocalDate.now().plusDays(1)
                    : calendario.getMesVisible().atDay(1) : LocalDate.parse(fecha.getText().trim());
            VentanaPrincipal ventana = (VentanaPrincipal) SwingUtilities.getWindowAncestor(this);
            ventana.abrirNuevoTurno(dia);
        } catch (DateTimeParseException e) {
            error(new IllegalArgumentException("Revisá la fecha antes de reservar."));
        }
    }

    private int numeroSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { throw new IllegalArgumentException("Seleccionar un turno de la tabla."); }
        return (Integer) modelo.getValueAt(fila, 0);
    }

    private void actualizarBotones() {
        int fila = tabla.getSelectedRow();
        boolean pendiente = fila != -1 && modelo.getValueAt(fila, 5).equals(EstadoTurno.PENDIENTE);
        cancelar.setEnabled(pendiente); atender.setEnabled(pendiente); correo.setEnabled(pendiente);
    }

    private void cambiarEstado(boolean registrarAtencion) {
        try {
            int numero = numeroSeleccionado();
            int fila = tabla.getSelectedRow();
            String pregunta = registrarAtencion ? "¿Marcar este turno como atendido?" : "¿Cancelar este turno?";
            String detalle = "\n\nPaciente: " + modelo.getValueAt(fila, 3)
                    + "\nMédico: " + modelo.getValueAt(fila, 4)
                    + "\nFecha: " + modelo.getValueAt(fila, 1) + "   Hora: " + modelo.getValueAt(fila, 2);
            if (!confirmar(pregunta + detalle)) { return; }
            if (registrarAtencion) { app.getGestor().atenderTurno(numero); }
            else { app.getGestor().cancelarTurno(numero); }
            filtrar(); informar(registrarAtencion ? "Atencion registrada." : "Turno cancelado. El horario queda libre.");
        } catch (TurnoException e) { error(e); }
        catch (IllegalArgumentException e) { error(e); }
    }

    private void mostrarCorreo() {
        try {
            app.getGestor().enviarRecordatorio(numeroSeleccionado());
            JTextArea mensaje = new JTextArea(app.getAvisoCorreo(), 12, 54);
            mensaje.setEditable(false); mensaje.setLineWrap(true); mensaje.setWrapStyleWord(true);
            mensaje.setCaretPosition(0);
            JOptionPane.showMessageDialog(this, new JScrollPane(mensaje), "Recordatorio simulado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (TurnoException e) { error(e); }
        catch (IllegalArgumentException e) { error(e); }
    }

    private void filtrar() {
        try {
            LocalDate fechaElegida = fecha.getText().trim().isEmpty() ? null : LocalDate.parse(fecha.getText().trim());
            Object medico = medicos.getSelectedItem(); Object paciente = pacientes.getSelectedItem();
            Object estado = estados.getSelectedItem();
            ArrayList<Turno> turnosCalendario = new ArrayList<Turno>();
            tabla.clearSelection(); modelo.setRowCount(0);
            for (Turno turno : app.getGestor().consultarTurnos()) {
                if (medico instanceof Medico && !turno.getMedico().getMatricula().equals(((Medico) medico).getMatricula())) { continue; }
                if (paciente instanceof Paciente && !turno.getPaciente().getDni().equals(((Paciente) paciente).getDni())) { continue; }
                if (!"Todos".equals(estado) && !turno.getEstado().name().equals(estado)) { continue; }
                turnosCalendario.add(turno);
                if (fechaElegida != null && !turno.getFecha().equals(fechaElegida)) { continue; }
                modelo.addRow(new Object[]{turno.getId(), turno.getFecha(), turno.getHora(),
                        turno.getPaciente().getNombre(), turno.getMedico().getNombre(), turno.getEstado()});
            }
            if (fechaElegida != null) { calendario.mostrarMes(YearMonth.from(fechaElegida)); }
            calendario.actualizarTurnos(turnosCalendario);
            boolean sinFiltros = medicos.getSelectedIndex() == 0 && pacientes.getSelectedIndex() == 0
                    && fechaElegida == null && estados.getSelectedIndex() == 0;
            resultado.setText(modelo.getRowCount() == 0 ? sinFiltros
                    ? "Todavía no hay turnos. Registrá el primero desde Pedir turno."
                    : "No encontramos turnos con estos filtros. Probá Limpiar filtros."
                    : "Turnos encontrados: " + modelo.getRowCount() + " · Seleccioná una fila para continuar.");
            if (vistaCalendario) {
                resultado.setText("Elegí un día para consultar sus turnos o reservar. Verde: pendiente o atendido · Rojo: cancelado.");
            }
            actualizarBotones();
        } catch (DateTimeParseException e) {
            error(new IllegalArgumentException("La fecha del filtro debe tener el formato aaaa-mm-dd."));
        }
    }

    public void actualizar() {
        Object medico = medicos.getSelectedItem(); Object paciente = pacientes.getSelectedItem();
        medicos.removeAllItems(); pacientes.removeAllItems(); medicos.addItem("Todos"); pacientes.addItem("Todos");
        for (Medico actual : app.getMedicos().listar()) {
            medicos.addItem(actual); if (actual == medico) { medicos.setSelectedItem(actual); }
        }
        for (Paciente actual : app.getPacientes().listar()) {
            pacientes.addItem(actual); if (actual == paciente) { pacientes.setSelectedItem(actual); }
        }
        filtrar();
    }
}
