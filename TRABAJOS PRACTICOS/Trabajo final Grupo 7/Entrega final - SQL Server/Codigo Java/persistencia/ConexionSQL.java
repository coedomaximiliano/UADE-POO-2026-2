package persistencia;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionSQL {
    public static Connection abrir() throws SQLException {
        Properties config = new Properties();
        Path carpeta = carpetaAplicacion();
        try (InputStream archivo = Files.newInputStream(carpeta.resolve("clinica.properties"))) {
            config.load(archivo);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            throw new SQLException(
                    "No se pudo leer clinica.properties o cargar el controlador JDBC.", e);
        }
        String url =
                "jdbc:sqlserver://"
                        + config.getProperty("servidor", "127.0.0.1")
                        + ":"
                        + config.getProperty("puerto", "14330");
        Properties opciones = new Properties();
        opciones.setProperty("databaseName", config.getProperty("base", "ClinicaGrupo7"));
        opciones.setProperty("encrypt", config.getProperty("encrypt", "true"));
        opciones.setProperty(
                "trustServerCertificate", config.getProperty("trustServerCertificate", "false"));
        opciones.setProperty("loginTimeout", "5");
        opciones.setProperty("socketTimeout", "10000");
        opciones.setProperty("applicationName", "Clinica Grupo 7");
        opciones.setProperty("sendTimeAsDatetime", "false");
        if (Boolean.parseBoolean(config.getProperty("integrada", "true"))) {
            opciones.setProperty("integratedSecurity", "true");
        } else {
            // Las credenciales opcionales se leen del entorno, nunca del codigo.
            String usuario = System.getenv("CLINICA_SQL_USUARIO");
            String clave = System.getenv("CLINICA_SQL_CLAVE");
            if (usuario == null || clave == null)
                throw new SQLException("Faltan las credenciales SQL en el entorno.");
            opciones.setProperty("user", usuario);
            opciones.setProperty("password", clave);
        }
        return DriverManager.getConnection(url, opciones);
    }

    public static Path carpetaAplicacion() {
        String ruta = System.getProperty("clinica.dir");
        if (ruta != null) return Path.of(ruta).toAbsolutePath();
        try {
            Path origen =
                    Path.of(
                            ConexionSQL.class
                                    .getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .toURI());
            return Files.isDirectory(origen) ? origen.getParent() : origen.getParent();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo ubicar la carpeta de la aplicacion.", e);
        }
    }

    public static void comprobar() {
        try (Connection c = abrir();
                var s = c.prepareStatement("SELECT TOP(0) id FROM dbo.Turnos")) {
            s.executeQuery().close();
        } catch (SQLException e) {
            throw error("conectar con la base", e);
        }
    }

    public static IllegalArgumentException error(String accion, SQLException e) {
        String detalle;
        switch (e.getErrorCode()) {
            case 50002:
                detalle = "El médico tiene una ausencia registrada en esa fecha.";
                break;
            case 50001:
                detalle = "El médico no atiende ese día u horario.";
                break;
            case 2601:
            case 2627:
                detalle = "El registro o el horario ya existe.";
                break;
            case 547:
                detalle = "El registro tiene turnos asociados o los datos no son validos.";
                break;
            case 2628:
            case 8152:
                detalle = "Alguno de los campos supera el largo permitido.";
                break;
            default:
                detalle =
                        "Revisar SQL Server, permisos y clinica.properties. Codigo: "
                                + e.getErrorCode();
        }
        return new IllegalArgumentException("No se pudo " + accion + ". " + detalle, e);
    }
}
