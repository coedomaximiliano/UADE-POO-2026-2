package ej1;

import java.time.LocalDate;

public class OrdenDeCompra {
	private int code;
	private	Producto product;
	private int amount;
	private Proveedor supplier;
	private LocalDate date;
	
	public OrdenDeCompra(int code, Producto product, int amount, Proveedor supplier, LocalDate date) {
		this.code = code;
		this.product = product;
		this.amount = amount;
		this.supplier = supplier;
		this.date = date;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Producto getProduct() {
		return product;
	}
	public void setProduct(Producto product) {
		this.product = product;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Proveedor getSupplier() {
		return supplier;
	}
	public void setSupplier(Proveedor supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return "Orden de Compra | Proovedor : %s | Producto : %s | Cantidad : %d".formatted(supplier,product,amount);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
	        return true;
		if (!(obj instanceof OrdenDeCompra other))
			return false;
		return this.code == other.code;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(code);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
