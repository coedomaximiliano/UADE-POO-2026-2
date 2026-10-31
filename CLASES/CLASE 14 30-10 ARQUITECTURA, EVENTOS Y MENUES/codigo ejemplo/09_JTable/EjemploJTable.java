import javax.swing.*;

// Diapositiva 26 - JTable (tabla)
public class EjemploJTable {
    public static void main(String[] args) {
        // Datos de ejemplo
        String[][] datos = {
            {"1", "Juan", "25"},
            {"2", "Ana", "30"},
            {"3", "Luis", "22"}
        };

        // Encabezados de columna
        String[] columnas = {"ID", "Nombre", "Edad"};

        // Crear la tabla
        JTable tabla = new JTable(datos, columnas);

        // Agregar la tabla a un JScrollPane (para scroll automático)
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Crear ventana
        JFrame ventana = new JFrame("Ejemplo JTable");
        ventana.setSize(400, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(scrollPane); // Agregar el scrollPane a la ventana
        ventana.setVisible(true);
    }
}
