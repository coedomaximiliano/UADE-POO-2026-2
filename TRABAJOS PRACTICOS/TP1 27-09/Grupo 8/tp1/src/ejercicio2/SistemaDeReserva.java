package ejercicio2;

import java.util.ArrayList;
import java.util.List;

public class SistemaDeReserva {
	private List<Vuelo> flights;
	private List<Pasaje> tickets = new ArrayList<Pasaje>();

	private int nextTicketNumber = 1;

	public SistemaDeReserva(List<Vuelo> flights) {
		this.flights = flights;
	}

	public SistemaDeReserva(List<Vuelo> flights, List<Pasaje> tickets) {
		this.flights = flights;
		this.tickets = tickets;
		for (Pasaje ticket : tickets)
			if (ticket.getTicketNumber() >= nextTicketNumber)
				nextTicketNumber = ticket.getTicketNumber() + 1;
	}


	public int nextTicketNumber() {
    	return nextTicketNumber++;
	}

	public List<Vuelo> getFlights() {
		return flights;
	}

	public void setFlights(List<Vuelo> flights) {
		this.flights = flights;
	}

	public List<Pasaje> getTickets() {
		return tickets;
	}

	public void setTickets(List<Pasaje> tickets) {
		this.tickets = tickets;
	}

	public Pasaje recordReserve(Pasaje ticket) {
		for (Vuelo flight : flights) {
			if (ticket.getFlightCode() == flight.getFlightCode()) {
				String key = ticket.isFirstClass() + "-" + ticket.isSmoker();
				switch (key) {
				case "true-true":
					flight.setFirstClassSmoker(flight.getFirstClassSmoker() - 1);
					break;
				case "true-false":
					flight.setFirstClassNonSmoker(flight.getFirstClassNonSmoker() - 1);
					break;
				case "false-true":
					flight.setEconomySmoker(flight.getEconomySmoker() - 1);
					break;
				case "false-false":
					flight.setEconomyNonSmoker(flight.getEconomyNonSmoker() - 1);
					break;
				default:
					break;
				}
				tickets.add(ticket);
				return ticket;
			}
		}
		System.out.println("Error: Vuelo no encontrado");
		return null;
	}

	public boolean removeReserve(Pasaje ticketToRemove) {
		if (tickets.remove(ticketToRemove)) {
			for (Vuelo flight : flights) {
				if (flight.getFlightCode() != ticketToRemove.getFlightCode())
					continue;
				String key = ticketToRemove.isFirstClass() + "-" + ticketToRemove.isSmoker();
				switch (key) {
				case "true-true":
					flight.setFirstClassSmoker(flight.getFirstClassSmoker() + 1);
					break;
				case "true-false":
					flight.setFirstClassNonSmoker(flight.getFirstClassNonSmoker() + 1);
					break;
				case "false-true":
					flight.setEconomySmoker(flight.getEconomySmoker() + 1);
					break;
				case "false-false":
					flight.setEconomyNonSmoker(flight.getEconomyNonSmoker() + 1);
					break;
				default:
					break;
				}
			}
			return true;
		}
		return false;
	}
}
