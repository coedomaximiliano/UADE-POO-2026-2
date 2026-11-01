package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Medico extends Persona {
    private String matricula;
    private String especialidad;
    private String consultorio;
    private ArrayList<HorarioAtencion> horarios;

    public Medico(String nombre, String dni, String email, String matricula, String especialidad) {
        this(
                nombre,
                dni,
                email,
                matricula,
                especialidad,
                "Sin asignar",
                new ArrayList<HorarioAtencion>());
    }

    public Medico(
            String nombre,
            String dni,
            String email,
            String matricula,
            String especialidad,
            String consultorio,
            List<HorarioAtencion> horarios) {
        super(nombre, dni, email);
        if (matricula == null
                || matricula.trim().isEmpty()
                || especialidad == null
                || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("Matricula y especialidad son obligatorias.");
        }
        this.matricula = matricula.trim();
        this.especialidad = especialidad.trim();
        if (consultorio == null || consultorio.trim().isEmpty() || horarios == null)
            throw new IllegalArgumentException("Falta el consultorio o los horarios.");
        this.consultorio = consultorio.trim();
        this.horarios = new ArrayList<HorarioAtencion>(horarios);
        java.util.HashSet<Integer> dias = new java.util.HashSet<Integer>();
        for (HorarioAtencion h : horarios)
            if (h == null || !dias.add(h.getDia()))
                throw new IllegalArgumentException("Indicar un solo bloque por día.");
    }

    public String getConsultorio() {
        return consultorio;
    }

    public ArrayList<HorarioAtencion> getHorarios() {
        return new ArrayList<HorarioAtencion>(horarios);
    }

    public boolean atiende(LocalDate fecha, LocalTime hora) {
        for (HorarioAtencion h : horarios) if (h.incluye(fecha, hora)) return true;
        return false;
    }

    public String mostrarHorarios() {
        if (horarios.isEmpty()) return "Sin horarios asignados";
        StringBuilder texto = new StringBuilder();
        for (HorarioAtencion h : horarios) {
            if (texto.length() > 0) texto.append(" / ");
            texto.append(h);
        }
        return texto.toString();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void actualizarEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        this.especialidad = especialidad.trim();
    }

    @Override
    public String mostrarDatos() {
        return getNombre() + " - Matricula: " + matricula + " - " + especialidad;
    }
}
