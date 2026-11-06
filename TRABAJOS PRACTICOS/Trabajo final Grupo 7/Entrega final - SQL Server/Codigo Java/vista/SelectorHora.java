package vista;

import java.time.LocalTime;
import java.util.List;

import javax.swing.JComboBox;

public class SelectorHora extends JComboBox<String> {
    public SelectorHora() {
        setName("horaTurno");
        setEditable(false);
        setMaximumRowCount(10);
        EstiloClinica.combo(this);
        getAccessibleContext().setAccessibleName("Horarios disponibles del turno");
        setToolTipText("Desplegá la lista y elegí uno de los horarios libres.");
        addItem("Sin horarios disponibles");
        setEnabled(false);
    }

    public void setHorariosDisponibles(List<LocalTime> horas) {
        Object anterior = getSelectedItem();
        removeAllItems();
        for (LocalTime h : horas) addItem(h.toString());
        if (horas.isEmpty()) {
            addItem("Sin horarios disponibles");
            setEnabled(false);
        } else {
            setEnabled(true);
            for (int i = 0; i < getItemCount(); i++) {
                if (getItemAt(i).equals(anterior)) {
                    setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public LocalTime getHoraSeleccionada() {
        if (!isEnabled() || getSelectedItem() == null) {
            throw new IllegalArgumentException("No hay horarios disponibles para ese día.");
        }
        return LocalTime.parse(getSelectedItem().toString());
    }

    public void enfocar() {
        requestFocusInWindow();
    }
}
