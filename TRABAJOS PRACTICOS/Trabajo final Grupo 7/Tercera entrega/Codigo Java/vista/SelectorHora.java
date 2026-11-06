package vista;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.*;

public class SelectorHora extends JSpinner {
    public SelectorHora() {
        ArrayList<String> horarios = new ArrayList<String>();
        for (int hora = 0; hora < 24; hora++) {
            horarios.add(String.format("%02d:00", hora));
            horarios.add(String.format("%02d:30", hora));
        }
        setModel(new SpinnerListModel(horarios) {
            public Object getNextValue() {
                int siguiente = (getList().indexOf(getValue()) + 1) % getList().size();
                return getList().get(siguiente);
            }
            public Object getPreviousValue() {
                int anterior = (getList().indexOf(getValue()) - 1 + getList().size()) % getList().size();
                return getList().get(anterior);
            }
        });
        setName("horaTurno"); setValue("10:00");
        setPreferredSize(new Dimension(180, 40));
        setBorder(BorderFactory.createLineBorder(EstiloClinica.BORDE));
        getAccessibleContext().setAccessibleName("Hora del turno en pasos de treinta minutos");
        final JFormattedTextField texto = ((DefaultEditor) getEditor()).getTextField();
        texto.setEditable(false); texto.setHorizontalAlignment(JTextField.CENTER);
        texto.setFont(texto.getFont().deriveFont(18f));
        texto.setBackground(java.awt.Color.WHITE); texto.setForeground(EstiloClinica.TEXTO);
        texto.setToolTipText("Usá las flechas, el teclado o la rueda del mouse. Cada paso cambia 30 minutos.");
        texto.getInputMap().put(KeyStroke.getKeyStroke("UP"), "subirHora");
        texto.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "bajarHora");
        texto.getActionMap().put("subirHora", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { setValue(getNextValue()); }
        });
        texto.getActionMap().put("bajarHora", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { setValue(getPreviousValue()); }
        });
        MouseWheelListener rueda = new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                for (int i = 0; i < Math.abs(e.getWheelRotation()); i++) {
                    setValue(e.getWheelRotation() < 0 ? getNextValue() : getPreviousValue());
                }
                e.consume();
            }
        };
        addMouseWheelListener(rueda); texto.addMouseWheelListener(rueda);
    }

    public LocalTime getHoraSeleccionada() {
        return LocalTime.parse(getValue().toString());
    }

    public void enfocar() {
        ((DefaultEditor) getEditor()).getTextField().requestFocusInWindow();
    }
}
