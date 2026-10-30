package ejercicio2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Vuelo {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private LocalDateTime takeOffDateTime;
	private String destination;
	private String origin;
	private int flightCode;
	private int firstClassSmoker;
	private int firstClassNonSmoker;
	private int economySmoker;
	private int economyNonSmoker;

	public Vuelo(LocalDateTime date, String destination, String origin, int flightCode, int fCS, int fCNS, int eS,
			int eNS) {
		this.takeOffDateTime = date;
		this.destination = destination;
		this.origin = origin;
		this.flightCode = flightCode;
		this.firstClassSmoker = fCS;
		this.firstClassNonSmoker = fCNS;
		this.economySmoker = eS;
		this.economyNonSmoker = eNS;
	}

	public LocalDateTime getTakeOffDateTime() {
		return takeOffDateTime;
	}

	public void setTakeOffDateTime(LocalDateTime takeOffDateTime) {
		this.takeOffDateTime = takeOffDateTime;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getFlightCode() {
		return flightCode;
	}

	public void setFlightCode(int flightCode) {
		this.flightCode = flightCode;
	}

	public int getFirstClassSmoker() {
		return firstClassSmoker;
	}

	public void setFirstClassSmoker(int firstClassSmoker) {
		this.firstClassSmoker = firstClassSmoker;
	}

	public int getFirstClassNonSmoker() {
		return firstClassNonSmoker;
	}

	public void setFirstClassNonSmoker(int firstClassNonSmoker) {
		this.firstClassNonSmoker = firstClassNonSmoker;
	}

	public int getEconomySmoker() {
		return economySmoker;
	}

	public void setEconomySmoker(int economySmoker) {
		this.economySmoker = economySmoker;
	}

	public int getEconomyNonSmoker() {
		return economyNonSmoker;
	}

	public void setEconomyNonSmoker(int economyNonSmoker) {
		this.economyNonSmoker = economyNonSmoker;
	}
	
	@Override
	public String toString() {
		return "Numero de vuelo:%d | Fecha de Despegue:%s | Origen:%s | Destino:%s".formatted(flightCode, takeOffDateTime.format(FORMATTER),origin,destination);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if (!(obj instanceof Vuelo other))
			return false;
		
		return this.flightCode == other.flightCode;
	}

}
