package ej2;

import java.util.*;

public class Vuelo {
	private Aeropuerto origin;
	private Aeropuerto destination;
	private List<Aeropuerto> layovers;
	private String flightCode;
	private Avion plane;
	private Map<String, Pasajero> seats = new HashMap<>();
	private List<Tripulante> crew;

	public Vuelo(Aeropuerto origin, Aeropuerto destination, List<Aeropuerto> layovers, String flightCode, Avion plane, List<Tripulante> crew) {
		validarTripulacion(crew);
		this.origin = origin;
		this.destination = destination;
		this.layovers = layovers;
		this.flightCode = flightCode;
		this.plane = plane;
		this.crew = crew;
	}

	public Vuelo(Aeropuerto origin, Aeropuerto destination, String flightCode, Avion plane, List<Tripulante> crew) {
		this(origin, destination, new ArrayList<>(), flightCode, plane, crew);
	}

	public Aeropuerto getOrigin() { return origin; }
	public void setOrigin(Aeropuerto origin) { this.origin = origin; }

	public Aeropuerto getDestination() { return destination; }
	public void setDestination(Aeropuerto destination) { this.destination = destination; }

	public List<Aeropuerto> getLayovers() { return layovers; }
	public void setLayovers(List<Aeropuerto> layovers) { this.layovers = layovers; }

	public String getFlightCode() {
		return flightCode;
	}

	public void setFlightCode(String flightCode) {
		this.flightCode = flightCode;
	}

	public Avion getPlane() {
		return plane;
	}

	public void setPlane(Avion plane) {
		this.plane = plane;
	}

	public List<Tripulante> getCrew() {
		return crew;
	}

	public void setCrew(List<Tripulante> crew) {
		validarTripulacion(crew);
		this.crew = crew;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Vuelo other))
			return false;
		return Objects.equals(flightCode, other.flightCode);
	}
	
	private String stringifyLayovers()
	{
		String result = "";
		for (Aeropuerto airport : layovers)
			result += "(%s) -> ".formatted(airport.getCode());
		return result;
	}
		
	@Override
	public String toString() {
		if(!(layovers.isEmpty()))
			return "Vuelo con escalas %s | Origen: %s | Destino: %s | Ruta Prevista: (%s) -> %s (%s)".formatted(flightCode,origin,destination,origin,stringifyLayovers(),destination);
		return "Vuelo directo %s | Origen: %s | Destino: %s | Ruta Prevista: (%s) -> (%s)".formatted(flightCode,origin,destination,origin,destination);
	}


	public void asignarAsiento(Pasajero pasajero, String asiento) {
		if (seats.size() >= plane.getCapacity())
			throw new IllegalStateException("El avion esta completo");
		if (seats.containsKey(asiento))
			throw new IllegalArgumentException("El asiento " + asiento + " ya esta ocupado");
		if (seats.containsValue(pasajero))
			throw new IllegalArgumentException("El pasajero ya tiene un asiento en este vuelo");
		seats.put(asiento, pasajero);
	}


	private static int contar(List<Tripulante> crew, Tripulante.job puesto) {
		int total = 0;
		for (Tripulante t : crew)
			if (t.getJob() == puesto)
				total++;
		return total;
	}

	private static void validarTripulacion(List<Tripulante> crew) {
		if (contar(crew, Tripulante.job.PILOT) != 2)
			throw new IllegalArgumentException("Se requieren exactamente 2 pilotos");
		if (contar(crew, Tripulante.job.COMUNICATION_OPERATOR) != 1)
			throw new IllegalArgumentException("Se requiere exactamente 1 operador de comunicaciones");
		if (contar(crew, Tripulante.job.FLIGHT_ATTENDANT) != 2)
			throw new IllegalArgumentException("Se requieren exactamente 2 comisarios de abordo");
		if (contar(crew, Tripulante.job.STEWARDESS) != 4)
			throw new IllegalArgumentException("Se requieren exactamente 4 azafatas");
	}

}
