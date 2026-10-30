package ej2;

import java.util.Objects;

public class Ciudad {
	private String name;
	private String country;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Ciudad(String name, String country) {
		this.name = name;
		this.country = country;
	}

	@Override
	public String toString() {
		return "Ciudad %s | Pais %s".formatted(name,country);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Ciudad other))
			return false;
		return Objects.equals(country, other.country) && Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, country);
	}
	
}
