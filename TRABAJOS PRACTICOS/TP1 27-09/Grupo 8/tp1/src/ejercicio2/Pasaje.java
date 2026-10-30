package ejercicio2;

public class Pasaje {
	private int flightCode;
	private String name;
	private String surname;
	private boolean firstClass;
	private boolean smoker;
	private int ticketNumber;
	
	public Pasaje(int ticketNumber,int flightCode,String name, String surname,boolean firstClass,boolean smoker) {
		this.ticketNumber = ticketNumber;
		this.flightCode = flightCode;
		this.name = name;
		this.surname = surname;
		this.firstClass = firstClass;
		this.smoker = smoker;
	}

	public int getTicketNumber() {
		return ticketNumber;
	}

	public int getFlightCode() {
		return flightCode;
	}

	public void setFlightCode(int flightCode) {
		this.flightCode = flightCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isFirstClass() {
		return firstClass;
	}

	public void setFirstClass(boolean firstClass) {
		this.firstClass = firstClass;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	@Override
	public String toString() {
		return  "Pasaje Nro %d | Numero de vuelo: %d | Nombre Completo: %s %s | Clase : %s | Zona fumadores : %s".formatted(ticketNumber,flightCode,name,surname
				,firstClass ? "Primera Clase" : "Economica",smoker ? "si" : "no");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Pasaje other))
			return false;

		return this.ticketNumber == other.ticketNumber;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(ticketNumber);
	}

}
