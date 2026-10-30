package ejercicio1;

import java.util.Set;

public class AgendaPersonal {
	private Set<Reunion> reuniones;
	
	public AgendaPersonal(Set<Reunion> reuniones) {
		this.reuniones = reuniones;
	}
	
	public void agregarReunion(Reunion reunion)
	{
		if(reunion != null)
			reuniones.add(reunion);
	}
	
	public void eliminarReunion(Reunion reunion)
	{
		reuniones.remove(reunion);
	}
	
	public void modificarReunion(Reunion reunion,int duracion,String lugar,String tema,Set<Persona> participantes)
	{
		reuniones.remove(reunion);
		reuniones.add(new Reunion(lugar,tema,participantes,duracion));
	}
	
	public String toString() {
		String result = "";
		for(Reunion reunion : reuniones)
			result += "%s\n".formatted(reunion);
		return result;
	}
}
