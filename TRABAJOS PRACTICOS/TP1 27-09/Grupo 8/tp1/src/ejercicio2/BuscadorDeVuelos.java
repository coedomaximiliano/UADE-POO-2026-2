package ejercicio2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BuscadorDeVuelos {

	public List<Vuelo> buscarVuelos(List<Vuelo> flights,boolean smoker, boolean firstClass, LocalDateTime earliestDate,
			LocalDateTime latestDate, String destination, String origin) {
		List<Vuelo> result = new ArrayList<Vuelo>();
		for (Vuelo flight : flights) {
			if (!(destination.equalsIgnoreCase(flight.getDestination())))
				continue;
			if (!(origin.equalsIgnoreCase(flight.getOrigin())))
				continue;
			if (flight.getTakeOffDateTime().compareTo(earliestDate) < 0
					|| flight.getTakeOffDateTime().compareTo(latestDate) > 0)
				continue;
			String key = firstClass + "-" + smoker;
			boolean availableSeating = false;
			switch (key) {
			case "true-true":
				availableSeating = flight.getFirstClassSmoker() > 0;
				break;
			case "true-false":
				availableSeating = flight.getFirstClassNonSmoker() > 0;
				break;
			case "false-true":
				availableSeating = flight.getEconomySmoker() > 0;
				break;
			case "false-false":
				availableSeating = flight.getEconomyNonSmoker() > 0;
				break;
			default:
				break;
			}
			if (!(availableSeating))
				continue;
			result.add(flight);
		}
		return result;
	}

}
