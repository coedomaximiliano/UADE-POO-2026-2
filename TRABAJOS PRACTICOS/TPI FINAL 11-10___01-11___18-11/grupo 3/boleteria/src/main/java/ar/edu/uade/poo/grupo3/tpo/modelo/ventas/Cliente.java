package ar.edu.uade.poo.grupo3.tpo.modelo.ventas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/** Cliente que compra entradas. Se identifica por DNI y email. */
public class Cliente {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s|]+@[^@\\s|]+\\.[^@\\s|]+$");

    private final String dni;
    private final String nombre;
    private final String email;
    private final List<Compra> compras = new ArrayList<>();

    public Cliente(String dni, String nombre, String email) {
        if (dni == null || !dni.trim().matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos");
        }
        if (nombre == null || nombre.isBlank() || nombre.contains("|")) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        this.dni = dni.trim();
        this.nombre = nombre.trim();
        this.email = email.trim().toLowerCase();
    }

    public void agregarCompra(Compra compra) {
        if (compra != null && !compras.contains(compra)) {
            compras.add(compra);
        }
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public List<Compra> getCompras() {
        return Collections.unmodifiableList(compras);
    }

    @Override
    public String toString() {
        return nombre + " (DNI " + dni + ")";
    }
}
