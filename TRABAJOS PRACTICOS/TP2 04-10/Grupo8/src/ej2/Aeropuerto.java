package ej2;

import java.util.Objects;

public class Aeropuerto {
	private String code;
	private Ciudad city;
	private String name;

	public Aeropuerto(String code, Ciudad city, String name) {
		this.code = code;
		this.city = city;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Ciudad getCity() {
		return city;
	}

	public void setCity(Ciudad city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Aeropuerto %s | Codigo: %s | Ciudad: %s".formatted(name, code, city)  ;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Aeropuerto other))
			return false;
		return Objects.equals(code, other.code);
	}
	
}
