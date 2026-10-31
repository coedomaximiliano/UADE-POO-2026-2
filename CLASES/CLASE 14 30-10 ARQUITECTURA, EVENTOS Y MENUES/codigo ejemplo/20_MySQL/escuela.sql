-- Diapositiva 53 - Base de datos de prueba para MySQL
CREATE DATABASE escuela;
USE escuela;

CREATE TABLE alumnos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    edad INT
);
