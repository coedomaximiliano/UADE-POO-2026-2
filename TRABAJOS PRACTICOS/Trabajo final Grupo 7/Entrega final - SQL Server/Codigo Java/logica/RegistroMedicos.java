package logica;

import modelo.*;

import persistencia.ConexionSQL;

import java.sql.*;
import java.util.*;

public class RegistroMedicos {
    private ArrayList<Medico> consultar(String matricula) {
        LinkedHashMap<String, String[]> datos = new LinkedHashMap<String, String[]>();
        HashMap<String, ArrayList<HorarioAtencion>> horarios =
                new HashMap<String, ArrayList<HorarioAtencion>>();
        String sql =
                "SELECT m.*,h.dia,h.desde,h.hasta FROM dbo.Medicos m LEFT JOIN dbo.HorariosMedico h ON h.matricula=m.matricula "
                        + (matricula == null ? "" : "WHERE m.matricula=? ")
                        + "ORDER BY m.nombre,m.matricula,h.dia";
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s = c.prepareStatement(sql)) {
            if (matricula != null) s.setString(1, matricula);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    String id = r.getString("matricula");
                    if (!datos.containsKey(id)) {
                        datos.put(
                                id,
                                new String[] {
                                    r.getString("nombre"),
                                    r.getString("dni"),
                                    r.getString("email"),
                                    id,
                                    r.getString("especialidad"),
                                    r.getString("consultorio")
                                });
                        horarios.put(id, new ArrayList<HorarioAtencion>());
                    }
                    int dia = r.getInt("dia");
                    if (!r.wasNull())
                        horarios.get(id)
                                .add(
                                        new HorarioAtencion(
                                                dia,
                                                r.getTime("desde").toLocalTime(),
                                                r.getTime("hasta").toLocalTime()));
                }
            }
        } catch (SQLException e) {
            throw ConexionSQL.error("consultar médicos y horarios", e);
        }
        ArrayList<Medico> lista = new ArrayList<Medico>();
        for (String id : datos.keySet()) {
            String[] v = datos.get(id);
            lista.add(new Medico(v[0], v[1], v[2], v[3], v[4], v[5], horarios.get(id)));
        }
        return lista;
    }

    public ArrayList<Medico> listar() {
        return consultar(null);
    }

    public Medico buscarPorMatricula(String matricula) {
        ArrayList<Medico> lista = consultar(matricula);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void agregar(Medico medico) {
        guardar(medico, false);
    }

    public void modificar(Medico medico) {
        guardar(medico, true);
    }

    private void guardar(Medico m, boolean modificar) {
        if (m == null
                || m.getHorarios().isEmpty()
                || m.getConsultorio().equalsIgnoreCase("Sin asignar")) {
            throw new IllegalArgumentException(
                    "Indicar consultorio y al menos un día de atención.");
        }
        try (Connection c = ConexionSQL.abrir()) {
            c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            c.setAutoCommit(false);
            try {
                String sql =
                        modificar
                                ? "UPDATE dbo.Medicos SET nombre=?,email=?,especialidad=?,consultorio=? WHERE matricula=? AND dni=?"
                                : "INSERT INTO dbo.Medicos(nombre,email,especialidad,consultorio,matricula,dni) VALUES(?,?,?,?,?,?)";
                try (PreparedStatement s = c.prepareStatement(sql)) {
                    s.setString(1, m.getNombre());
                    s.setString(2, m.getEmail());
                    s.setString(3, m.getEspecialidad());
                    s.setString(4, m.getConsultorio());
                    s.setString(5, m.getMatricula());
                    s.setString(6, m.getDni());
                    if (s.executeUpdate() != 1)
                        throw new IllegalArgumentException(
                                "El médico no existe o cambió su identificador.");
                }
                try (PreparedStatement s =
                        c.prepareStatement("DELETE FROM dbo.HorariosMedico WHERE matricula=?")) {
                    s.setString(1, m.getMatricula());
                    s.executeUpdate();
                }
                try (PreparedStatement s =
                        c.prepareStatement(
                                "INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(?,?,?,?)")) {
                    for (HorarioAtencion h : m.getHorarios()) {
                        s.setString(1, m.getMatricula());
                        s.setInt(2, h.getDia());
                        s.setTime(3, Time.valueOf(h.getDesde()));
                        s.setTime(4, Time.valueOf(h.getHasta()));
                        s.addBatch();
                    }
                    s.executeBatch();
                }
                // Antes de confirmar, revisamos que la nueva jornada respete los turnos cargados.
                String revisar =
                        "SELECT COUNT(*) FROM dbo.Turnos t WHERE t.medico_matricula=? AND t.estado='PENDIENTE' "
                                + "AND (t.fecha>CONVERT(date,GETDATE()) OR (t.fecha=CONVERT(date,GETDATE()) AND t.hora>CONVERT(time,GETDATE()))) "
                                + "AND NOT EXISTS(SELECT 1 FROM dbo.HorariosMedico h WHERE h.matricula=t.medico_matricula "
                                + "AND h.dia=DATEDIFF(DAY,CONVERT(date,'19000101'),t.fecha)%7+1 AND t.hora>=h.desde AND t.hora<=DATEADD(MINUTE,-30,h.hasta))";
                try (PreparedStatement s = c.prepareStatement(revisar)) {
                    s.setString(1, m.getMatricula());
                    try (ResultSet r = s.executeQuery()) {
                        r.next();
                        if (r.getInt(1) > 0)
                            throw new IllegalArgumentException(
                                    "Hay turnos pendientes fuera del nuevo horario. Mantener esa franja o cancelar esos turnos antes.");
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                // Si algo falla, recuperamos tanto los datos del médico como sus horarios
                // anteriores.
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ConexionSQL.error("guardar médico y horarios", e);
        }
    }

    public void eliminar(String matricula) {
        try (Connection c = ConexionSQL.abrir();
                PreparedStatement s =
                        c.prepareStatement("DELETE FROM dbo.Medicos WHERE matricula=?")) {
            s.setString(1, matricula);
            if (s.executeUpdate() != 1)
                throw new IllegalArgumentException("El médico ya no existe.");
        } catch (SQLException e) {
            throw ConexionSQL.error("eliminar médico (revisar turnos o pacientes de cabecera)", e);
        }
    }
}
