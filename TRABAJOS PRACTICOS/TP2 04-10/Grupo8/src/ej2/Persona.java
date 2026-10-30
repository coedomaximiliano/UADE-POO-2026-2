package ej2;


public abstract class Persona {
	private int DNI;
	private String name;
	private String surname;
	private String gender;
	private int age;

	public Persona(int DNI,String name, String surname, String gender, int age) {
		this.DNI = DNI;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.age = age;
	}

	public int getDNI() {
		return DNI;
	}

	public void setDNI(int dNI) {
		DNI = dNI;
	}

	public String getName() {
		return this.name;
	}

	public String getSurname() {
		return this.surname;
	}

	public String getGender() {
		return this.gender;
	}

	public int getAge() {
		return this.age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "%s %s sexo: %s edad: %d".formatted(getName(), getSurname(), getGender(), getAge());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		return DNI == other.DNI;
	}

}