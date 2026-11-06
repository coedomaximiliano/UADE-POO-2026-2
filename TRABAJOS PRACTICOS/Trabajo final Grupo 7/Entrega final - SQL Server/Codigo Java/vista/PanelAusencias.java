package vista;

import aplicacion.AplicacionClinica;

import logica.RegistroAusencias;

import modelo.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelAusencias extends PanelPantalla {
    private RegistroAusencias registro = new RegistroAusencias();
    private JComboBox<Medico> medicos = new JComboBox<>();
    private SelectorFecha desde = new SelectorFecha(), hasta = new SelectorFecha();
    private JComboBox<String> motivo =
            new JComboBox<>(new String[] {"Vacaciones", "Licencia", "Otro"});
    private DefaultTableModel periodos =
            modelo(new String[] {"N°", "Médico", "Desde", "Hasta", "Motivo", "Estado"});
    private DefaultTableModel reservas =
            modelo(
                    new String[] {
                        "N°", "Fecha", "Hora", "Paciente", "Correo", "Médico", "Consultorio"
                    });
    private JTable tablaPeriodos = tabla(periodos, "tablaAusencias"),
            tablaAfectados = tabla(reservas, "tablaTurnosAfectados");
    private ArrayList<Turno> afectados = new ArrayList<>();
    private JLabel resumen = new JLabel();
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    public PanelAusencias(AplicacionClinica app, String matricula) {
        super(
                app,
                "Ausencias y turnos a reasignar",
                "Registrá vacaciones o licencias. Las reservas existentes se conservan hasta que decidas qué hacer.");
        medicos.setName("medicoAusencia");
        EstiloClinica.combo(medicos);
        EstiloClinica.combo(motivo);
        for (Medico m : app.getMedicos().listar()) {
            medicos.addItem(m);
            if (m.getMatricula().equals(matricula)) medicos.setSelectedItem(m);
        }
        desde.setName("ausenciaDesde");
        hasta.setName("ausenciaHasta");
        desde.setFechaSeleccionada(LocalDate.now());
        hasta.setFechaSeleccionada(LocalDate.now());
        JPanel form = new JPanel(new GridLayout(2, 4, 12, 8));
        for (String t : new String[] {"Médico", "Desde (inclusive)", "Hasta (inclusive)", "Motivo"})
            form.add(new JLabel(t));
        form.add(medicos);
        form.add(desde);
        form.add(hasta);
        form.add(motivo);
        JPanel alta = new JPanel(new BorderLayout(0, 10));
        alta.add(form, BorderLayout.CENTER);
        alta.add(
                acciones(boton("Registrar ausencia", "guardarAusencia", e -> guardar())),
                BorderLayout.SOUTH);
        JTabbedPane fichas = new JTabbedPane();
        JPanel pendientes = new JPanel(new BorderLayout(0, 8));
        pendientes.add(resumen, BorderLayout.NORTH);
        pendientes.add(new JScrollPane(tablaAfectados), BorderLayout.CENTER);
        pendientes.add(
                acciones(
                        boton("Reasignar seleccionado", "reasignarAfectado", e -> reasignar()),
                        boton("Actualizar", "actualizarAusencias", e -> actualizar())),
                BorderLayout.SOUTH);
        fichas.addTab("Turnos a reasignar · todos los médicos", pendientes);
        JPanel historial = new JPanel(new BorderLayout());
        historial.add(new JScrollPane(tablaPeriodos), BorderLayout.CENTER);
        historial.add(
                acciones(boton("Anular ausencia seleccionada", "anularAusencia", e -> anular())),
                BorderLayout.SOUTH);
        fichas.addTab("Períodos registrados", historial);
        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.add(alta, BorderLayout.NORTH);
        centro.add(fichas, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        actualizar();
    }

    private void guardar() {
        try {
            Medico m = (Medico) medicos.getSelectedItem();
            if (m == null) throw new IllegalArgumentException("Primero registrá un médico.");
            AusenciaMedica a =
                    new AusenciaMedica(
                            0,
                            m.getMatricula(),
                            desde.getFechaSeleccionada(),
                            hasta.getFechaSeleccionada(),
                            motivo.getSelectedItem().toString(),
                            true);
            if (!confirmar(
                    "Registrar "
                            + a.getMotivo()
                            + " de "
                            + m.getNombre()
                            + "\nDesde "
                            + a.getDesde().format(formato)
                            + " hasta "
                            + a.getHasta().format(formato)
                            + "\n\nSe bloquearán nuevas reservas. Los turnos existentes quedarán para revisión."))
                return;
            registro.guardar(a);
            actualizar();
            refrescarResumen();
            informar(
                    "Ausencia registrada. Revisá los turnos a reasignar y avisá a los pacientes antes de cambiar sus reservas.");
        } catch (IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void anular() {
        try {
            int fila = tablaPeriodos.getSelectedRow();
            if (fila < 0)
                throw new IllegalArgumentException("Seleccioná una ausencia en la tabla.");
            if (!confirmar(
                    "¿Anular esta ausencia?\nLas fechas volverán a estar disponibles según la agenda. Los turnos reasignados no se modifican."))
                return;
            registro.anular((Integer) periodos.getValueAt(fila, 0));
            actualizar();
            refrescarResumen();
        } catch (IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void reasignar() {
        try {
            int fila = tablaAfectados.getSelectedRow();
            if (fila < 0) throw new IllegalArgumentException("Seleccioná un turno para reasignar.");
            new DialogoReasignar(
                            this,
                            app,
                            afectados.get(fila),
                            () -> {
                                actualizar();
                                refrescarResumen();
                            })
                    .setVisible(true);
        } catch (IllegalArgumentException ex) {
            error(ex);
        }
    }

    private void refrescarResumen() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof JDialog) w = w.getOwner();
        if (w instanceof VentanaPrincipal) ((VentanaPrincipal) w).actualizarResumen();
    }

    public void actualizar() {
        ArrayList<AusenciaMedica> ausencias = registro.listar();
        ArrayList<Turno> nuevos = registro.afectados();
        periodos.setRowCount(0);
        reservas.setRowCount(0);
        afectados = nuevos;
        for (AusenciaMedica a : ausencias) {
            Medico m = app.getMedicos().buscarPorMatricula(a.getMatricula());
            periodos.addRow(
                    new Object[] {
                        a.getId(),
                        m == null ? a.getMatricula() : m.getNombre(),
                        a.getDesde().format(formato),
                        a.getHasta().format(formato),
                        a.getMotivo(),
                        a.isActiva() ? "Activa" : "Anulada"
                    });
        }
        for (Turno t : afectados)
            reservas.addRow(
                    new Object[] {
                        t.getId(),
                        t.getFecha().format(formato),
                        t.getHora(),
                        t.getPaciente().getNombre(),
                        t.getPaciente().getEmail(),
                        t.getMedico().getNombre(),
                        t.getMedico().getConsultorio()
                    });
        resumen.setText(
                afectados.size()
                        + " turno(s) pendientes afectados. Reasigná cada uno y comunicá el cambio de forma manual.");
    }

    public static void abrir(Component origen, AplicacionClinica app, String matricula) {
        try {
            JDialog d =
                    new JDialog(
                            SwingUtilities.getWindowAncestor(origen),
                            "Ausencias médicas",
                            Dialog.ModalityType.APPLICATION_MODAL);
            d.setName("dialogoAusencias");
            d.setContentPane(new PanelAusencias(app, matricula));
            d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            d.setSize(1200, 720);
            d.setMinimumSize(new Dimension(1000, 650));
            d.setLocationRelativeTo(origen);
            d.setVisible(true);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    origen, ex.getMessage(), "Revisar ausencias", JOptionPane.ERROR_MESSAGE);
        }
    }
}
