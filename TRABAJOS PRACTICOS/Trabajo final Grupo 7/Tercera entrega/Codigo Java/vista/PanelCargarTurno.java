package vista;

import aplicacion.AplicacionClinica;
import logica.TurnoException;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.awt.Component;
import javax.swing.*;

public class PanelCargarTurno extends PanelPantalla {
    private JComboBox<Paciente> pacientes = new JComboBox<Paciente>();
    private JComboBox<Medico> medicos = new JComboBox<Medico>();
    private SelectorFecha fecha = new SelectorFecha();
    private SelectorHora hora = new SelectorHora();

    public PanelCargarTurno(AplicacionClinica app) {
        super(app, "Pedir turno", "Elegí paciente, médico y horario. Al guardar vas a ver la confirmación de la reserva.");
        pacientes.setName("pacienteTurno"); medicos.setName("medicoTurno");
        EstiloClinica.combo(pacientes); EstiloClinica.combo(medicos);
        JPanel filas = new JPanel(new GridLayout(2, 2, 20, 20));
        filas.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(EstiloClinica.BORDE),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        filas.add(grupo("Paciente", pacientes)); filas.add(grupo("Médico", medicos));
        filas.add(grupo("Fecha", fecha)); filas.add(grupo("Hora · cada 30 minutos", hora));
        hora.setToolTipText("Subí o bajá de a 30 minutos con las flechas.");
        JPanel contenido = new JPanel(new BorderLayout(12, 20));
        contenido.add(filas, BorderLayout.NORTH);
        contenido.add(acciones(boton("Guardar turno", "guardarTurno", new ActionListener() {
            public void actionPerformed(ActionEvent e) { guardar(); }
        })), BorderLayout.CENTER);
        JLabel ayuda = new JLabel("¿Falta una persona? Agregala con el botón + en Pacientes o Médicos.");
        ayuda.setForeground(EstiloClinica.SECUNDARIO);
        contenido.add(ayuda, BorderLayout.SOUTH);
        add(contenido, BorderLayout.CENTER);
        actualizar();
    }

    private JPanel grupo(String texto, Component control) {
        JPanel grupo = new JPanel(new BorderLayout(0, 8));
        JLabel etiqueta = new JLabel(texto); etiqueta.setLabelFor(control);
        grupo.add(etiqueta, BorderLayout.NORTH); grupo.add(control, BorderLayout.CENTER);
        return grupo;
    }

    private void guardar() {
        try {
            Paciente paciente = (Paciente) pacientes.getSelectedItem();
            Medico medico = (Medico) medicos.getSelectedItem();
            if (paciente == null || medico == null) {
                throw new TurnoException("Primero registrar un paciente y un medico.");
            }
            Turno turno = app.getGestor().cargarTurno(paciente.getDni(), medico.getMatricula(),
                    fecha.getFechaSeleccionada(), hora.getHoraSeleccionada());
            JTextArea mensaje = new JTextArea("Turno registrado correctamente\n\nNúmero: " + turno.getId()
                    + "\nPaciente: " + turno.getPaciente().getNombre()
                    + "\nMédico: " + turno.getMedico().getNombre()
                    + "\nFecha: " + turno.getFecha() + "   Hora: " + turno.getHora()
                    + "\n\nRecordatorio de prueba\n" + app.getAvisoCorreo(), 15, 48);
            mensaje.setEditable(false); mensaje.setLineWrap(true); mensaje.setWrapStyleWord(true);
            mensaje.setCaretPosition(0);
            JOptionPane.showMessageDialog(this, new JScrollPane(mensaje), "Turno registrado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (TurnoException e) { error(e); }
        catch (IllegalArgumentException e) { error(e); }
    }

    public void actualizar() {
        Paciente pacienteAnterior = (Paciente) pacientes.getSelectedItem();
        Medico medicoAnterior = (Medico) medicos.getSelectedItem();
        pacientes.removeAllItems(); medicos.removeAllItems();
        for (Paciente paciente : app.getPacientes().listar()) {
            pacientes.addItem(paciente);
            if (paciente == pacienteAnterior) { pacientes.setSelectedItem(paciente); }
        }
        for (Medico medico : app.getMedicos().listar()) {
            medicos.addItem(medico);
            if (medico == medicoAnterior) { medicos.setSelectedItem(medico); }
        }
    }

    public void seleccionarFecha(LocalDate dia) {
        fecha.setFechaSeleccionada(dia);
        hora.enfocar();
    }
}
