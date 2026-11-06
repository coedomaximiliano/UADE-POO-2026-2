package vista;

import modelo.HorarioAtencion;

import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.*;

public class EditorHorarioMedico extends JPanel {
    private JCheckBox[] dias = new JCheckBox[7];
    private ArrayList<JComboBox<String>> desde = new ArrayList<JComboBox<String>>();
    private ArrayList<JComboBox<String>> hasta = new ArrayList<JComboBox<String>>();

    public EditorHorarioMedico() {
        setLayout(new GridLayout(8, 3, 10, 5));
        add(new JLabel("Día de atención"));
        add(new JLabel("Desde"));
        add(new JLabel("Hasta"));
        String[] nombres = {
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        };
        for (int i = 0; i < 7; i++) {
            final int fila = i;
            dias[i] = new JCheckBox(nombres[i]);
            dias[i].setName("diaMedico-" + (i + 1));
            JComboBox<String> inicio = new JComboBox<String>();
            JComboBox<String> fin = new JComboBox<String>();
            for (int h = 0; h < 24; h++)
                for (int m = 0; m < 60; m += 30) {
                    String texto = String.format("%02d:%02d", h, m);
                    inicio.addItem(texto);
                    fin.addItem(texto);
                }
            inicio.setSelectedItem("08:00");
            fin.setSelectedItem("12:00");
            inicio.setName("desdeMedico-" + (i + 1));
            fin.setName("hastaMedico-" + (i + 1));
            desde.add(inicio);
            hasta.add(fin);
            inicio.setEnabled(false);
            fin.setEnabled(false);
            dias[i].addActionListener(
                    e -> {
                        desde.get(fila).setEnabled(dias[fila].isSelected());
                        hasta.get(fila).setEnabled(dias[fila].isSelected());
                    });
            add(dias[i]);
            add(inicio);
            add(fin);
        }
    }

    public ArrayList<HorarioAtencion> getHorarios() {
        ArrayList<HorarioAtencion> lista = new ArrayList<HorarioAtencion>();
        for (int i = 0; i < 7; i++)
            if (dias[i].isSelected())
                lista.add(
                        new HorarioAtencion(
                                i + 1,
                                LocalTime.parse(desde.get(i).getSelectedItem().toString()),
                                LocalTime.parse(hasta.get(i).getSelectedItem().toString())));
        if (lista.isEmpty())
            throw new IllegalArgumentException("Elegí al menos un día de atención.");
        return lista;
    }

    public void setHorarios(java.util.List<HorarioAtencion> lista) {
        for (int i = 0; i < 7; i++) {
            dias[i].setSelected(false);
            desde.get(i).setEnabled(false);
            hasta.get(i).setEnabled(false);
        }
        for (HorarioAtencion h : lista) {
            int i = h.getDia() - 1;
            dias[i].setSelected(true);
            desde.get(i).setEnabled(true);
            hasta.get(i).setEnabled(true);
            desde.get(i).setSelectedItem(h.getDesde().toString());
            hasta.get(i).setSelectedItem(h.getHasta().toString());
        }
    }
}
