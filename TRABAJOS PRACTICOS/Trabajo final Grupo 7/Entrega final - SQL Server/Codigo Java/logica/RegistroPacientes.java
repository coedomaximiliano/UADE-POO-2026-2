package logica;

import modelo.Paciente;

import persistencia.ConexionSQL;

import java.sql.*;
import java.util.ArrayList;

public class RegistroPacientes {
    private Paciente leer(ResultSet r) throws SQLException {
        return new Paciente(
                r.getString("nombre"),
                r.getString("dni"),
                r.getString("email"),
                r.getString("historia"),
                r.getString("cabecera"));
    }

    public void agregar(Paciente datos) {
        if (datos == null) throw new IllegalArgumentException("Faltan los datos.");
        String sql =
                "INSERT INTO dbo.Pacientes (dni,nombre,email,historia,cabecera) VALUES (?,?,?,?,?)";
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, datos.getDni());
            s.setString(2, datos.getNombre());
            s.setString(3, datos.getEmail());
            s.setString(4, datos.getNumeroHistoriaClinica());
            s.setString(5, datos.getMedicoCabecera());
            s.executeUpdate();
        } catch (SQLException e) {
            throw ConexionSQL.error("registrar paciente", e);
        }
    }

    public Paciente buscarPorDni(String id) {
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement("SELECT * FROM dbo.Pacientes WHERE dni=?")) {
            s.setString(1, id);
            try (ResultSet r = s.executeQuery()) {
                return r.next() ? leer(r) : null;
            }
        } catch (SQLException e) {
            throw ConexionSQL.error("consultar paciente", e);
        }
    }

    public ArrayList<Paciente> listar() {
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement("SELECT * FROM dbo.Pacientes ORDER BY nombre,dni");
                ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(leer(r));
            return lista;
        } catch (SQLException e) {
            throw ConexionSQL.error("listar pacientes", e);
        }
    }

    public void modificar(Paciente datos) {
        if (datos == null) throw new IllegalArgumentException("Faltan los datos.");
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement(
                                "UPDATE dbo.Pacientes SET nombre=?,email=?,historia=?,cabecera=? WHERE dni=?")) {
            s.setString(1, datos.getNombre());
            s.setString(2, datos.getEmail());
            s.setString(3, datos.getNumeroHistoriaClinica());
            s.setString(4, datos.getMedicoCabecera());
            s.setString(5, datos.getDni());
            if (s.executeUpdate() != 1)
                throw new IllegalArgumentException(
                        "El registro no existe o se cambio su identificador.");
        } catch (SQLException e) {
            throw ConexionSQL.error("modificar paciente", e);
        }
    }

    public void eliminar(String id) {
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement("DELETE FROM dbo.Pacientes WHERE dni=?")) {
            s.setString(1, id);
            if (s.executeUpdate() != 1)
                throw new IllegalArgumentException("El registro ya no existe.");
        } catch (SQLException e) {
            throw ConexionSQL.error("eliminar paciente", e);
        }
    }
}
