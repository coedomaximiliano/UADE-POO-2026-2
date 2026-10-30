package ej1;

import java.util.Set;

public class Proveedor {
	private int DNI;
	private String fullName;
	private Set<Producto> products;

	public Proveedor(int DNI, String fullName, Set<Producto> products) {
		this.DNI = DNI;
		this.fullName = fullName;
		this.products = products;
	}

	public int getDNI() {
		return DNI;
	}

	public void setDNI(int dNI) {
		DNI = dNI;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Set<Producto> getProducts() {
		return products;
	}

	public void setProducts(Set<Producto> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		String result = "Proveedor %s | DNI: %d\nLista de productos:\n".formatted(fullName, DNI);

		for (Producto product : products) {
			result += product + "\n";
		}

		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
	        return true;
		if (!(obj instanceof Proveedor other))
			return false;
		return this.DNI == other.DNI;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(DNI);
	}

}
