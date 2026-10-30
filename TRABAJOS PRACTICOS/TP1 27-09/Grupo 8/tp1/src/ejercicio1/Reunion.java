package ejercicio1;

import java.util.Set;

public class Reunion {
	private String lugar;
	private String tema;
	private Set<Persona> participantes;
	private int duracion;

	public Reunion(String lugar, String tema, Set<Persona> participantes, int duracion) {
		this.lugar = lugar;
		this.tema = tema;
		this.participantes = participantes;
		this.duracion = duracion;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public Set<Persona> getParticipantes() {
		return participantes;
	}

	public void addParticipante(Persona participante) {
		participantes.add(participante);
	}

	public void setParticipantes(Set<Persona> participantes) {
		this.participantes = participantes;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String stringifyParticipantes() {
		String result = "";
		int i = 1;
		for (Persona participante : participantes) {
			result += "Participante %d:%s\n".formatted(i, participante);
			i++;
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof Reunion other))
			return false;

		return this.lugar.equals(other.lugar) && this.duracion == other.duracion
				&& this.participantes.equals(other.participantes) && this.tema.equals(other.tema);
	}

	@Override
	public String toString() {
		return "Reunion [lugar=" + lugar + ", tema=" + tema + ", participantes=" + stringifyParticipantes()
				+ ", duracion=" + duracion + "]";
	}

}
