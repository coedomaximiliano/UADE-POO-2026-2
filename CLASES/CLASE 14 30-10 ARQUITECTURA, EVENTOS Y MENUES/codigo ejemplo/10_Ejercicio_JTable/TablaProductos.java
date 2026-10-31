import javax.swing.*;

// Diapositiva 28 - Respuesta ejercicio: JTable
public class TablaProductos {
    public static void main(String[] args) {
        // Datos de los productos
        String[][] productos = {
            {"001", "Laptop", "1200.00"},
            {"002", "Mouse", "25.50"},
            {"003", "Teclado", "45.00"},
            {"004", "Monitor", "300.00"}
        };

        // Nombres de las columnas
        String[] columnas = {"Código", "Nombre", "Precio"};

        // Crear tabla
        JTable tabla = new JTable(productos, columnas);

        // Scroll para la tabla
        JScrollPane scroll = new JScrollPane(tabla);

        // Crear ventana
        JFrame ventana = new JFrame("Tabla de Productos");
        ventana.setSize(500, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(scroll);
        ventana.setVisible(true);
    }
}
