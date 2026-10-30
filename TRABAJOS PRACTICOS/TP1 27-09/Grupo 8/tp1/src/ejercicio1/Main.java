package ejercicio1;

import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		AgendaPersonal agenda = new AgendaPersonal(new HashSet<>());

		Persona ana = new Persona("Facundo", "Garcia");
		Persona luis = new Persona("Luis", "Gomez");

		Set<Persona> participantes = new HashSet<>();
		participantes.add(ana);
		participantes.add(luis);

		Reunion reunion = new Reunion("Sala 1", "Planificacion del sprint", participantes, 60);

		agenda.agregarReunion(reunion);
		System.out.println(agenda);
	}
}