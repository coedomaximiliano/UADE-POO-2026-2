import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Diapositiva 54 - Entrada y salida de datos con MySQL (JDBC)
public class ConexionMySQL {
    public static void main(String[] args) {

        // Datos de conexión (ajustá el usuario/contraseña según tu MySQL)
        String url = "jdbc:mysql://localhost:3306/escuela";
        String user = "root";
        String password = "";  // si usás XAMPP, usualmente está vacío

        try {
            // Conectar a la base de datos
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(" Conectado correctamente a MySQL.");

            // Crear un Statement
            Statement stmt = conn.createStatement();

            // Insertar un registro
            String insertar = "INSERT INTO alumnos (nombre, edad) VALUES ('Ana López', 21)";
            stmt.executeUpdate(insertar);
            System.out.println("Registro insertado.");

            //  Consultar y mostrar resultados
            String consulta = "SELECT * FROM alumnos";
            ResultSet rs = stmt.executeQuery(consulta);

            System.out.println("\n Lista de alumnos:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int edad = rs.getInt("edad");
                System.out.println(id + " | " + nombre + " | " + edad + " años");
            }

            // Cerrar conexión
            rs.close();
            stmt.close();
            conn.close();
            System.out.println("\n Conexión cerrada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL:");
            e.printStackTrace();
        }
    }
}
