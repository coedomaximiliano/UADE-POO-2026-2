package logica;

import modelo.*;

import persistencia.ConexionSQL;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;

public class RegistroAusencias {
    public ArrayList<AusenciaMedica> listar() {
        ArrayList<AusenciaMedica> lista = new ArrayList<>();
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement(
                                "SELECT * FROM dbo.AusenciasMedico ORDER BY desde DESC,id DESC");
                ResultSet r = s.executeQuery()) {
            while (r.next())
                lista.add(
                        new AusenciaMedica(
                                r.getInt("id"),
                                r.getString("matricula"),
                                r.getDate("desde").toLocalDate(),
                                r.getDate("hasta").toLocalDate(),
                                r.getString("motivo"),
                                r.getBoolean("activa")));
            return lista;
        } catch (SQLException e) {
            throw ConexionSQL.error("consultar las ausencias", e);
        }
    }

    public void guardar(AusenciaMedica ausencia) {
        if (ausencia == null || ausencia.getHasta().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("La ausencia debe incluir hoy o una fecha futura.");
        try (Connection c = ConexionSQL.abrir()) {
            c.setAutoCommit(false);
            try {
                bloquearMedico(c, ausencia.getMatricula());
                try (PreparedStatement s =
                        c.prepareStatement(
                                "SELECT COUNT(*) FROM dbo.AusenciasMedico WHERE matricula=? AND activa=1 AND desde<=? AND hasta>=?")) {
                    s.setString(1, ausencia.getMatricula());
                    s.setDate(2, Date.valueOf(ausencia.getHasta()));
                    s.setDate(3, Date.valueOf(ausencia.getDesde()));
                    try (ResultSet r = s.executeQuery()) {
                        r.next();
                        if (r.getInt(1) > 0)
                            throw new IllegalArgumentException(
                                    "Ya existe una ausencia que se superpone con ese período.");
                    }
                }
                try (PreparedStatement s =
                        c.prepareStatement(
                                "INSERT INTO dbo.AusenciasMedico(matricula,desde,hasta,motivo) VALUES(?,?,?,?)")) {
                    s.setString(1, ausencia.getMatricula());
                    s.setDate(2, Date.valueOf(ausencia.getDesde()));
                    s.setDate(3, Date.valueOf(ausencia.getHasta()));
                    s.setString(4, ausencia.getMotivo());
                    s.executeUpdate();
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ConexionSQL.error("registrar la ausencia", e);
        }
    }

    private void bloquearMedico(Connection c, String matricula) throws SQLException {
        try (PreparedStatement s =
                c.prepareStatement(
                        "SELECT matricula FROM dbo.Medicos WITH(UPDLOCK,HOLDLOCK) WHERE matricula=?")) {
            s.setString(1, matricula);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) throw new IllegalArgumentException("No existe el médico.");
            }
        }
    }

    public void anular(int id) {
        // Se conserva el registro. Anular no mueve los turnos ya reasignados.
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement(
                                "UPDATE dbo.AusenciasMedico SET activa=0 WHERE id=? AND activa=1")) {
            s.setInt(1, id);
            if (s.executeUpdate() != 1)
                throw new IllegalArgumentException(
                        "La ausencia ya fue anulada. Actualizá la lista.");
        } catch (SQLException e) {
            throw ConexionSQL.error("anular la ausencia", e);
        }
    }

    public boolean ausente(String matricula, LocalDate fecha) {
        for (AusenciaMedica a : listar())
            if (a.getMatricula().equals(matricula) && a.incluye(fecha)) return true;
        return false;
    }

    public ArrayList<Turno> afectados() {
        return afectados(new RepositorioTurnos().consultar(), listar(), LocalDateTime.now());
    }

    public static ArrayList<Turno> afectados(
            ArrayList<Turno> turnos, ArrayList<AusenciaMedica> ausencias, LocalDateTime ahora) {
        ArrayList<Turno> lista = new ArrayList<>();
        for (Turno t : turnos) {
            // También incluye el turno en curso, hasta que termine su franja.
            if (t.getEstado() != EstadoTurno.PENDIENTE
                    || !LocalDateTime.of(t.getFecha(), t.getHora()).plusMinutes(30).isAfter(ahora))
                continue;
            for (AusenciaMedica a : ausencias)
                if (a.getMatricula().equals(t.getMedico().getMatricula())
                        && a.incluye(t.getFecha())) {
                    lista.add(t);
                    break;
                }
        }
        return lista;
    }
}
