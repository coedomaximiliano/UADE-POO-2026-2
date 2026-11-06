package ar.edu.uade.poo.grupo3.tpo.modelo.ventas;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.regex.Pattern;

/** Cliente con su cuenta de acceso: email y contraseña (se guarda solo el hash, nunca la contraseña). */
public class Cliente {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s|]+@[^@\\s|]+\\.[^@\\s|]+$");
    public static final int LARGO_MINIMO_CONTRASENIA = 6;

    private final String dni;
    private final String nombre;
    private final String email;
    private String hashContrasenia;
    private final List<Compra> compras = new ArrayList<>();

    /** Registra un cliente nuevo a partir de la contraseña en texto plano. */
    public Cliente(String dni, String nombre, String email, String contrasenia) {
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
        cambiarContrasenia(contrasenia);
    }

    /** Reconstruye un cliente leído del archivo (ya viene con el hash, no se vuelve a hashear). */
    public static Cliente desdeArchivo(String dni, String nombre, String email, String hashContrasenia) {
        Cliente cliente = new Cliente(dni, nombre, email, "temporal");
        cliente.hashContrasenia = hashContrasenia;
        return cliente;
    }

    public boolean verificarContrasenia(String contrasenia) {
        return contrasenia != null && hashContrasenia.equals(hashear(contrasenia));
    }

    public void cambiarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < LARGO_MINIMO_CONTRASENIA) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos " + LARGO_MINIMO_CONTRASENIA + " caracteres");
        }
        this.hashContrasenia = hashear(contrasenia);
    }

    private static String hashear(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
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

    public String getHashContrasenia() {
        return hashContrasenia;
    }

    public List<Compra> getCompras() {
        return Collections.unmodifiableList(compras);
    }

    @Override
    public String toString() {
        return nombre + " (DNI " + dni + ")";
    }
}
