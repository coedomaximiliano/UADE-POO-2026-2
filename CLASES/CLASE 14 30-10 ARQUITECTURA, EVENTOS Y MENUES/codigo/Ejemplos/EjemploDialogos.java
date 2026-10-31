import javax.swing.JOptionPane;

// JOptionPane: dialogos estandar (se pueden usar incluso sin ventana)
public class EjemploDialogos {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Operacion exitosa");
        JOptionPane.showMessageDialog(null, "No se encontro el archivo",
                "Error", JOptionPane.ERROR_MESSAGE);

        String nombre = JOptionPane.showInputDialog(null, "Ingrese su nombre:");

        int r = JOptionPane.showConfirmDialog(null, "Guardar cambios de " + nombre + "?",
                "Confirmar", JOptionPane.YES_NO_CANCEL_OPTION);

        String[] opciones = {"Efectivo", "Tarjeta", "Transferencia"};
        int elegido = JOptionPane.showOptionDialog(null, "Medio de pago", "Pago",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        System.out.println("Nombre=" + nombre + " confirm=" + r + " pago=" + elegido);
    }
}
