package ej1;

public class Producto {
	private int code;
	private double unitPrice;
	private String description;

	public Producto(int code, double unitPrice) {
		this.code = code;
		this.unitPrice = unitPrice;
		this.description = "No especificada";
	}

	public Producto(int code, double unitPrice, String description) {
		this.code = code;
		this.unitPrice = unitPrice;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Producto codigo %d | descripcion : %s | precio unitario: %.2f".formatted(code, description, unitPrice);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
	        return true;
		if (!(obj instanceof Producto other))
			return false;
		return this.code == other.code;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(code);
	}

}
