package logica;

import modelo.*;

import persistencia.ConexionSQL;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RepositorioTurnos implements AlmacenTurnos {
    private static final String CONSULTA =
            "SELECT t.*,p.nombre pn,p.email pe,p.historia,"
                    + "m.nombre mn,m.dni mdni,m.email me,m.especialidad,m.consultorio,p.cabecera FROM dbo.Turnos t "
                    + "JOIN dbo.Pacientes p ON t.paciente_dni=p.dni "
                    + "JOIN dbo.Medicos m ON t.medico_matricula=m.matricula ";

    @Override
    public int getProximoId() {
        // SQL Server asigna el numero para evitar IDs repetidos entre ventanas.
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement("SELECT NEXT VALUE FOR dbo.SecuenciaTurnos");
                ResultSet r = s.executeQuery()) {
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            throw ConexionSQL.error("asignar el numero del turno", e);
        }
    }

    @Override
    public void guardar(Turno turno) {
        if (turno == null) throw new IllegalArgumentException("Falta el turno.");
        String sql =
                "INSERT INTO dbo.Turnos(id,paciente_dni,medico_matricula,fecha,hora,estado) VALUES(?,?,?,?,?,?)";
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, turno.getId());
            s.setString(2, turno.getPaciente().getDni());
            s.setString(3, turno.getMedico().getMatricula());
            s.setDate(4, Date.valueOf(turno.getFecha()));
            s.setTime(5, Time.valueOf(turno.getHora()));
            s.setString(6, turno.getEstado().name());
            s.executeUpdate();
        } catch (SQLException e) {
            throw ConexionSQL.error("guardar el turno", e);
        }
    }

    private Turno leer(ResultSet r) throws SQLException {
        Paciente p =
                new Paciente(
                        r.getString("pn"),
                        r.getString("paciente_dni"),
                        r.getString("pe"),
                        r.getString("historia"),
                        r.getString("cabecera"));
        Medico m =
                new Medico(
                        r.getString("mn"),
                        r.getString("mdni"),
                        r.getString("me"),
                        r.getString("medico_matricula"),
                        r.getString("especialidad"),
                        r.getString("consultorio"),
                        new ArrayList<HorarioAtencion>());
        Turno t =
                new Turno(
                        r.getInt("id"),
                        r.getDate("fecha").toLocalDate(),
                        r.getTime("hora").toLocalTime(),
                        m,
                        p);
        try {
            if ("CANCELADO".equals(r.getString("estado"))) t.cancelar();
            if ("ATENDIDO".equals(r.getString("estado"))) t.atender();
        } catch (TurnoException e) {
            throw new SQLException("Estado de turno invalido.", e);
        }
        return t;
    }

    private ArrayList<Turno> buscar(String condicion, Object dato) {
        ArrayList<Turno> lista = new ArrayList<Turno>();
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement(
                                CONSULTA + condicion + " ORDER BY t.fecha,t.hora,t.id")) {
            if (dato != null) s.setObject(1, dato);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) lista.add(leer(r));
            }
            return lista;
        } catch (SQLException e) {
            throw ConexionSQL.error("consultar los turnos", e);
        }
    }

    @Override
    public Turno buscarPorId(int id) {
        ArrayList<Turno> lista = buscar("WHERE t.id=?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public ArrayList<Turno> consultar() {
        return buscar("", null);
    }

    @Override
    public ArrayList<Turno> consultarPorMedico(String matricula) {
        return buscar("WHERE t.medico_matricula=?", matricula);
    }

    @Override
    public ArrayList<Turno> consultarPorPaciente(String dni) {
        return buscar("WHERE t.paciente_dni=?", dni);
    }

    @Override
    public ArrayList<Turno> consultarPorFecha(LocalDate fecha) {
        return buscar("WHERE t.fecha=?", Date.valueOf(fecha));
    }

    @Override
    public boolean disponible(Medico medico, LocalDate fecha, LocalTime hora) {
        String sql =
                "SELECT COUNT(*) FROM dbo.Turnos WHERE medico_matricula=? AND fecha=? AND hora=? AND estado<>'CANCELADO'";
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, medico.getMatricula());
            s.setDate(2, Date.valueOf(fecha));
            s.setTime(3, Time.valueOf(hora));
            try (ResultSet r = s.executeQuery()) {
                r.next();
                return r.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw ConexionSQL.error("comprobar la disponibilidad", e);
        }
    }

    @Override
    public void actualizarEstado(Turno turno) throws TurnoException {
        // Se actualiza solo si sigue pendiente. Otra ventana puede haberlo cambiado.
        String sql = "UPDATE dbo.Turnos SET estado=? WHERE id=? AND estado='PENDIENTE'";
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, turno.getEstado().name());
            s.setInt(2, turno.getId());
            if (s.executeUpdate() != 1)
                throw new TurnoException("El turno ya cambio de estado. Actualizar la agenda.");
        } catch (SQLException e) {
            throw ConexionSQL.error("actualizar el turno", e);
        }
    }

    @Override
    public void reasignar(Turno anterior, Turno nuevo) throws TurnoException {
        // Una sola actualización: si el nuevo horario falla, el turno anterior sigue intacto.
        String sql =
                "UPDATE dbo.Turnos SET medico_matricula=?,fecha=?,hora=? WHERE id=? AND estado='PENDIENTE' AND medico_matricula=? AND fecha=? AND hora=?";
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, nuevo.getMedico().getMatricula());
            s.setDate(2, Date.valueOf(nuevo.getFecha()));
            s.setTime(3, Time.valueOf(nuevo.getHora()));
            s.setInt(4, anterior.getId());
            s.setString(5, anterior.getMedico().getMatricula());
            s.setDate(6, Date.valueOf(anterior.getFecha()));
            s.setTime(7, Time.valueOf(anterior.getHora()));
            if (s.executeUpdate() != 1)
                throw new TurnoException("El turno cambió en otra ventana. Actualizá la lista.");
        } catch (SQLException e) {
            throw ConexionSQL.error("reasignar el turno", e);
        }
    }
}
