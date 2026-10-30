package ej2;

public class Tripulante extends Persona {
	public enum job {
		PILOT, COMUNICATION_OPERATOR, STEWARDESS, FLIGHT_ATTENDANT
	}

	private int employeeNumber;
	private job job;

	public Tripulante(int DNI, String name, String surname, String gender, int age, int employeeNumber, job job) {
		super(DNI, name, surname, gender, age);
		this.employeeNumber = employeeNumber;
		this.job = job;
	}

	public int getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public job getJob() {
		return job;
	}

	public void setJob(job job) {
		this.job = job;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Tripulante other))
			return false;
		return this.employeeNumber == other.employeeNumber;
	}
	
	@Override
	public String toString() {
		return "%s %s %s | Numero de empleado: %d ".formatted(job.toString().replace("_", " "),getName(),getSurname(),employeeNumber);
	}
	
	
}
