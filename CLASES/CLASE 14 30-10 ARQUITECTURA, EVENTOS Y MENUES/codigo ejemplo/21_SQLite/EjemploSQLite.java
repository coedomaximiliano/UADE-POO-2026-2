import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Diapositivas 55 y 56 - Entrada y salida de datos con SQLite (JDBC)
public class EjemploSQLite {
    public static void main(String[] args) {
        Connection conexion = null;

        try {
            // 1) Conectar o crear la base de datos (archivo .db)
            String url = "jdbc:sqlite:ejemplo.db";
            conexion = DriverManager.getConnection(url);
            System.out.println("Conexión establecida con SQLite");

            // 2) Crear tabla si no existe
            String sqlCrearTabla = "CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "edad INTEGER"
                    + ");";

            Statement stmt = conexion.createStatement();
            stmt.execute(sqlCrearTabla);
            System.out.println("Tabla 'usuarios' creada/verificada.");

            // 3) Insertar datos
            String sqlInsert = "INSERT INTO usuarios(nombre, edad) VALUES ('Juan', 25);";
            stmt.executeUpdate(sqlInsert);
            System.out.println("Registro insertado.");

            // 4) Leer datos
            String sqlSelect = "SELECT id, nombre, edad FROM usuarios;";
            ResultSet rs = stmt.executeQuery(sqlSelect);

            System.out.println("\nDatos en la tabla usuarios:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("nombre") + " | " +
                    rs.getInt("edad")
                );
            }

            // 5) Cerrar recursos
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                    System.out.println("Conexión cerrada.");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
