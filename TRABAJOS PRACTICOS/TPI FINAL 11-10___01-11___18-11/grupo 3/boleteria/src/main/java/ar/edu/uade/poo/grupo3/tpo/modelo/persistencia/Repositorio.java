package ar.edu.uade.poo.grupo3.tpo.modelo.persistencia;

import java.io.IOException;
import java.util.List;

/**
 * Patrón Repository: el dominio no sabe cómo se guardan los datos.
 * Hoy se implementa con archivos .txt; mañana podría ser una base de datos sin tocar la Boleteria.
 */
public interface Repositorio<T> {

    List<T> cargarTodos() throws IOException;

    void guardarTodos(List<T> elementos) throws IOException;
}
