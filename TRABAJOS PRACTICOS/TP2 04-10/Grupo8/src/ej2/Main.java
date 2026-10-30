package ej2;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Ciudad buenosAires = new Ciudad("Buenos Aires", "Argentina");
		Ciudad santiago = new Ciudad("Santiago", "Chile");
		Ciudad madrid = new Ciudad("Madrid", "Espana");

		Aeropuerto ezeiza = new Aeropuerto("EZE", buenosAires, "Ezeiza");
		Aeropuerto aeroparque = new Aeropuerto("AEP", buenosAires, "Aeroparque");
		Aeropuerto scl = new Aeropuerto("SCL", santiago, "Arturo Merino Benitez");
		Aeropuerto mad = new Aeropuerto("MAD", madrid, "Barajas");


		Avion avion = new Avion("Boeing 737", 101, 3);


		System.out.println("=== Vuelo directo ===");
		Vuelo directo = new Vuelo(aeroparque, scl, "AR1234", avion, crearTripulacion());
		System.out.println(directo);


		System.out.println("\n=== Vuelo con escala ===");
		List<Aeropuerto> escalas = new ArrayList<>();
		escalas.add(scl);
		Vuelo conEscala = new Vuelo(ezeiza, mad, escalas, "AR1132", avion, crearTripulacion());
		System.out.println(conEscala);


		System.out.println("\n=== Asignacion de asientos (capacidad: 3) ===");
		Pasajero facundo = new Pasajero(30111222, "Facundo", "Garcia", "M", 25);
		Pasajero luis = new Pasajero(28999888, "Luis", "Gomez", "M", 41);
		Pasajero sofia = new Pasajero(35444555, "Sofia", "Perez", "F", 27);
		Pasajero juan = new Pasajero(33222111, "Juan", "Diaz", "M", 52);

		intentarAsignar(directo, facundo, "1A");
		intentarAsignar(directo, luis, "1B");
		intentarAsignar(directo, sofia, "1A");
		intentarAsignar(directo, facundo, "2C");
		intentarAsignar(directo, sofia, "2C");
		intentarAsignar(directo, juan, "3A");
	}

	private static void intentarAsignar(Vuelo vuelo, Pasajero pasajero, String asiento) {
		try {
			vuelo.asignarAsiento(pasajero, asiento);
			System.out.println("Asiento " + asiento + " asignado para: " + pasajero.getName());
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println("No se pudo ubicar al pasajero " + pasajero.getName() + ". " + e.getMessage());
		}
	}

	// 2 pilotos, 1 operador de comunicaciones, 2 comisarios de abordo y 4 azafatas
	private static List<Tripulante> crearTripulacion() {
		List<Tripulante> tripulacion = new ArrayList<>();
		tripulacion.add(new Tripulante(20000001, "Carlos", "Ruiz", "M", 45, 1, Tripulante.job.PILOT));
		tripulacion.add(new Tripulante(20000002, "Marta", "Silva", "F", 39, 2, Tripulante.job.PILOT));
		tripulacion.add(new Tripulante(20000003, "Pablo", "Vega", "M", 33, 3, Tripulante.job.COMUNICATION_OPERATOR));
		tripulacion.add(new Tripulante(20000004, "Diego", "Mora", "M", 36, 4, Tripulante.job.FLIGHT_ATTENDANT));
		tripulacion.add(new Tripulante(20000005, "Laura", "Paz", "F", 31, 5, Tripulante.job.FLIGHT_ATTENDANT));
		tripulacion.add(new Tripulante(20000006, "Julia", "Rey", "F", 28, 6, Tripulante.job.STEWARDESS));
		tripulacion.add(new Tripulante(20000007, "Rocio", "Sosa", "F", 26, 7, Tripulante.job.STEWARDESS));
		tripulacion.add(new Tripulante(20000008, "Camila", "Luna", "F", 29, 8, Tripulante.job.STEWARDESS));
		tripulacion.add(new Tripulante(20000009, "Valeria", "Ortiz", "F", 30, 9, Tripulante.job.STEWARDESS));
		return tripulacion;
	}
}
