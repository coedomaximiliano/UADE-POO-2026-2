package vista;

import aplicacion.AplicacionClinica;

import logica.*;

import modelo.*;

import java.awt.*;
import java.time.*;
import java.util.*;

import javax.swing.*;

public class DialogoReasignar extends JDialog {
    private JComboBox<Medico> medicos = new JComboBox<>();
    private SelectorFecha fecha = new SelectorFecha();
    private SelectorHora hora = new SelectorHora();
    private LinkedHashMap<LocalDate, ArrayList<LocalTime>> disponibles = new LinkedHashMap<>();
    private boolean cambiando;
    private JButton guardar = new JButton("Confirmar reasignación");
    private JLabel aviso = new JLabel(" ");

    public DialogoReasignar(
            Component origen, AplicacionClinica app, Turno turno, Runnable alGuardar) {
        super(
                SwingUtilities.getWindowAncestor(origen),
                "Reasignar turno N° " + turno.getId(),
                Dialog.ModalityType.APPLICATION_MODAL);
        setName("dialogoReasignar");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel contenido = new JPanel(new BorderLayout(16, 18));
        contenido.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        JPanel encabezado = new JPanel(new GridLayout(0, 1, 0, 8));
        encabezado.add(
                new JLabel(
                        "Paciente: "
                                + turno.getPaciente().getNombre()
                                + " · DNI "
                                + turno.getPaciente().getDni()));
        encabezado.add(
                new JLabel(
                        "Actual: "
                                + turno.getMedico().getNombre()
                                + " · "
                                + turno.getFecha()
                                + " · "
                                + turno.getHora()));
        encabezado.add(
                new JLabel(
                        "Especialidad: "
                                + turno.getMedico().getEspecialidad()
                                + ". Se conserva el número de turno."));
        contenido.add(encabezado, BorderLayout.NORTH);
        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 8));
        medicos.setName("medicoReasignacion");
        EstiloClinica.combo(medicos);
        for (Medico m : app.getMedicos().listar())
            if (m.getEspecialidad().equalsIgnoreCase(turno.getMedico().getEspecialidad()))
                medicos.addItem(m);
        campos.add(new JLabel("Médico (misma especialidad)"));
        campos.add(medicos);
        campos.add(new JLabel("Fecha disponible"));
        campos.add(fecha);
        campos.add(new JLabel("Hora disponible"));
        campos.add(hora);
        campos.add(aviso);
        contenido.add(campos, BorderLayout.CENTER);
        Runnable cargar =
                () -> {
                    try {
                        cambiando = true;
                        Medico m = (Medico) medicos.getSelectedItem();
                        disponibles =
                                m == null
                                        ? new LinkedHashMap<>()
                                        : new DisponibilidadMedica(
                                                        app.getMedicos(), new RepositorioTurnos())
                                                .consultar(
                                                        m.getMatricula(),
                                                        turno.getPaciente().getDni(),
                                                        LocalDate.now(),
                                                        180,
                                                        turno.getId());
                        fecha.setFechasDisponibles(disponibles.keySet());
                        if (!disponibles.isEmpty())
                            fecha.setFechaSeleccionada(disponibles.keySet().iterator().next());
                        hora.setHorariosDisponibles(
                                disponibles.getOrDefault(
                                        fecha.getFechaSeleccionada(), new ArrayList<>()));
                        guardar.setEnabled(!disponibles.isEmpty());
                        aviso.setText(
                                disponibles.isEmpty()
                                        ? "Sin disponibilidad en los próximos 180 días."
                                        : m.getConsultorio() + " · Elegí fecha y hora.");
                    } catch (IllegalArgumentException ex) {
                        disponibles.clear();
                        guardar.setEnabled(false);
                        aviso.setText(ex.getMessage());
                    } finally {
                        cambiando = false;
                    }
                };
        medicos.addActionListener(e -> cargar.run());
        fecha.addActionListener(
                e -> {
                    if (!cambiando)
                        hora.setHorariosDisponibles(
                                disponibles.getOrDefault(
                                        fecha.getFechaSeleccionada(), new ArrayList<>()));
                });
        guardar.setName("confirmarReasignacion");
        EstiloClinica.boton(guardar, true, false);
        guardar.addActionListener(
                e -> {
                    try {
                        Medico m = (Medico) medicos.getSelectedItem();
                        if (m == null) throw new IllegalArgumentException("Elegí un médico.");
                        String detalle =
                                "Turno N° "
                                        + turno.getId()
                                        + "\n"
                                        + turno.getPaciente().getNombre()
                                        + "\n"
                                        + m.getNombre()
                                        + " · "
                                        + m.getConsultorio()
                                        + "\n"
                                        + fecha.getFechaSeleccionada()
                                        + " · "
                                        + hora.getHoraSeleccionada()
                                        + "\n\nAvisá al paciente del cambio de forma manual.";
                        if (JOptionPane.showConfirmDialog(
                                        this,
                                        detalle,
                                        "Confirmar nuevo turno",
                                        JOptionPane.OK_CANCEL_OPTION)
                                != JOptionPane.OK_OPTION) return;
                        app.getGestor()
                                .reasignarTurno(
                                        turno.getId(),
                                        m.getMatricula(),
                                        fecha.getFechaSeleccionada(),
                                        hora.getHoraSeleccionada());
                        dispose();
                        alGuardar.run();
                        Window w = SwingUtilities.getWindowAncestor(origen);
                        if (w instanceof VentanaPrincipal)
                            ((VentanaPrincipal) w).actualizarResumen();
                    } catch (TurnoException | IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(
                                this,
                                ex.getMessage(),
                                "Revisar reasignación",
                                JOptionPane.ERROR_MESSAGE);
                        cargar.run();
                    }
                });
        JButton volver = new JButton("Volver");
        volver.addActionListener(e -> dispose());
        EstiloClinica.boton(volver, false, false);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(volver);
        botones.add(guardar);
        contenido.add(botones, BorderLayout.SOUTH);
        setContentPane(contenido);
        setSize(740, 570);
        setMinimumSize(new Dimension(640, 540));
        setLocationRelativeTo(origen);
        cargar.run();
    }
}
