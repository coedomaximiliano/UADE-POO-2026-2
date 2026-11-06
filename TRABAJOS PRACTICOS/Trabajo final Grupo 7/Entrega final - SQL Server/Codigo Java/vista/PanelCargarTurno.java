package vista;

import aplicacion.AplicacionClinica;

import logica.*;

import modelo.*;

import java.awt.*;
import java.time.*;
import java.util.*;

import javax.swing.*;

public class PanelCargarTurno extends PanelPantalla {
    private JComboBox<Paciente> pacientes = new JComboBox<Paciente>();
    private JComboBox<String> especialidades = new JComboBox<String>();
    private JComboBox<Medico> medicos = new JComboBox<Medico>();
    private JLabel cabecera = new JLabel("Sin médico de cabecera asignado");
    private JLabel datosMedico = new JLabel(" "), detalle = new JLabel(" ");
    private SelectorFecha fecha = new SelectorFecha();
    private SelectorHora hora = new SelectorHora();
    private JButton guardar;
    private boolean cargando;
    private ArrayList<Medico> profesionales = new ArrayList<Medico>();
    private LinkedHashMap<LocalDate, ArrayList<LocalTime>> libres =
            new LinkedHashMap<LocalDate, ArrayList<LocalTime>>();

    public PanelCargarTurno(AplicacionClinica app) {
        super(
                app,
                "Pedir turno",
                "Elegí paciente, especialidad y médico. Se muestran solo días y horarios disponibles.");
        pacientes.setName("pacienteTurno");
        especialidades.setName("especialidadTurno");
        medicos.setName("medicoTurno");
        cabecera.setName("cabeceraPaciente");
        datosMedico.setName("datosMedicoTurno");
        detalle.setName("detalleDisponibilidad");
        for (JComboBox<?> c : new JComboBox<?>[] {pacientes, especialidades, medicos}) {
            EstiloClinica.combo(c);
            c.setMaximumRowCount(12);
        }
        JPanel campos = new JPanel(new GridLayout(3, 2, 20, 16));
        campos.add(grupo("Paciente", pacientes));
        campos.add(grupo("Médico de cabecera del paciente", cabecera));
        campos.add(grupo("Especialidad", especialidades));
        campos.add(grupo("Médico", medicos));
        campos.add(grupo("Fecha disponible", fecha));
        campos.add(grupo("Hora disponible · cada 30 minutos", hora));
        campos.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(EstiloClinica.BORDE),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        JPanel centro = new JPanel(new BorderLayout(10, 18));
        centro.add(campos, BorderLayout.NORTH);
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 10));
        info.add(datosMedico);
        info.add(detalle);
        centro.add(info, BorderLayout.CENTER);
        guardar = boton("Guardar turno", "guardarTurno", e -> guardar());
        centro.add(
                acciones(
                        guardar,
                        boton(
                                "Actualizar disponibilidad",
                                "actualizarDisponibilidad",
                                e -> actualizar())),
                BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
        pacientes.addActionListener(
                e -> {
                    if (!cargando) {
                        mostrarCabecera();
                        recargarDisponibilidad();
                    }
                });
        especialidades.addActionListener(
                e -> {
                    if (!cargando) {
                        cargarMedicos(null);
                        recargarDisponibilidad();
                    }
                });
        medicos.addActionListener(
                e -> {
                    if (!cargando) recargarDisponibilidad();
                });
        fecha.addActionListener(
                e -> {
                    if (!cargando) mostrarHoras();
                });
        actualizar();
    }

    private JPanel grupo(String nombre, Component c) {
        JPanel panel = new JPanel(new BorderLayout(0, 7));
        JLabel etiqueta = new JLabel(nombre);
        etiqueta.setLabelFor(c);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(c, BorderLayout.CENTER);
        return panel;
    }

    private void mostrarCabecera() {
        Paciente p = (Paciente) pacientes.getSelectedItem();
        Medico encontrado = null;
        if (p != null)
            for (Medico m : profesionales)
                if (m.getMatricula().equals(p.getMedicoCabecera())) encontrado = m;
        cabecera.setText(
                encontrado == null
                        ? "Sin médico de cabecera asignado"
                        : encontrado.getNombre() + " · " + encontrado.getEspecialidad());
        cabecera.setToolTipText(cabecera.getText());
    }

    private void cargarMedicos(String preferido) {
        boolean antes = cargando;
        cargando = true;
        medicos.removeAllItems();
        String especialidad = (String) especialidades.getSelectedItem();
        for (Medico m : profesionales)
            if (m.getEspecialidad().equals(especialidad)) {
                medicos.addItem(m);
                if (m.getMatricula().equals(preferido)) medicos.setSelectedItem(m);
            }
        cargando = antes;
    }

    private void recargarDisponibilidad() {
        try {
            Paciente p = (Paciente) pacientes.getSelectedItem();
            Medico m = (Medico) medicos.getSelectedItem();
            libres.clear();
            if (p != null && m != null) {
                libres =
                        new DisponibilidadMedica(app.getMedicos(), new RepositorioTurnos())
                                .consultar(m.getMatricula(), p.getDni(), LocalDate.now(), 180);
                datosMedico.setText(m.getConsultorio() + " · " + m.mostrarHorarios());
                datosMedico.setToolTipText(datosMedico.getText());
            } else datosMedico.setText("Registrá un paciente y un médico para reservar.");
            boolean antes = cargando;
            cargando = true;
            fecha.setFechasDisponibles(libres.keySet());
            if (!libres.isEmpty() && !libres.containsKey(fecha.getFechaSeleccionada()))
                fecha.setFechaSeleccionada(libres.keySet().iterator().next());
            cargando = antes;
            mostrarHoras();
        } catch (IllegalArgumentException e) {
            cargando = false;
            guardar.setEnabled(false);
            error(e);
        }
    }

    private void mostrarHoras() {
        ArrayList<LocalTime> horas = libres.get(fecha.getFechaSeleccionada());
        hora.setHorariosDisponibles(horas == null ? new ArrayList<LocalTime>() : horas);
        guardar.setEnabled(horas != null && !horas.isEmpty());
        detalle.setText(
                libres.isEmpty()
                        ? "Sin disponibilidad en los próximos 180 días. Revisá los horarios del médico."
                        : "Calendario: solo días con cupos. "
                                + libres.size()
                                + " días disponibles en los próximos 180 días.");
    }

    private void guardar() {
        try {
            Paciente p = (Paciente) pacientes.getSelectedItem();
            Medico m = (Medico) medicos.getSelectedItem();
            if (p == null || m == null) throw new TurnoException("Elegí paciente y médico.");
            Turno t =
                    app.getGestor()
                            .cargarTurno(
                                    p.getDni(),
                                    m.getMatricula(),
                                    fecha.getFechaSeleccionada(),
                                    hora.getHoraSeleccionada());
            recargarDisponibilidad();
            java.awt.Window ventana = SwingUtilities.getWindowAncestor(this);
            if (ventana instanceof VentanaPrincipal)
                ((VentanaPrincipal) ventana).actualizarResumen();
            new ConfirmacionTurno(ventana, t).setVisible(true);
        } catch (TurnoException | IllegalArgumentException e) {
            recargarDisponibilidad();
            error(e);
        }
    }

    public void actualizar() {
        Paciente antes = (Paciente) pacientes.getSelectedItem();
        Medico anterior = (Medico) medicos.getSelectedItem();
        String especialidad = (String) especialidades.getSelectedItem();
        cargando = true;
        try {
            profesionales = app.getMedicos().listar();
            pacientes.removeAllItems();
            especialidades.removeAllItems();
            for (Paciente p : app.getPacientes().listar()) {
                pacientes.addItem(p);
                if (antes != null && antes.getDni().equals(p.getDni()))
                    pacientes.setSelectedItem(p);
            }
            TreeSet<String> nombres = new TreeSet<String>();
            for (Medico m : profesionales) nombres.add(m.getEspecialidad());
            for (String s : nombres) especialidades.addItem(s);
            if (especialidad != null && nombres.contains(especialidad))
                especialidades.setSelectedItem(especialidad);
            cargarMedicos(anterior == null ? null : anterior.getMatricula());
            mostrarCabecera();
        } finally {
            cargando = false;
        }
        recargarDisponibilidad();
    }

    public void seleccionarFecha(LocalDate dia) {
        if (libres.containsKey(dia)) fecha.setFechaSeleccionada(dia);
        else
            detalle.setText(
                    "Ese día no tiene horarios libres para el médico elegido. Seleccioná un día habilitado.");
        hora.enfocar();
    }
}
