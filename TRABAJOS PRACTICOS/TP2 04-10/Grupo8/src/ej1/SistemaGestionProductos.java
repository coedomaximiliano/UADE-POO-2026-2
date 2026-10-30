package ej1;

import java.util.HashMap;

public class SistemaGestionProductos {
	private HashMap<Integer, Producto> products;
	private HashMap<Integer, Proveedor> suppliers;
	private HashMap<Integer, OrdenDeCompra> purchaseOrders;

	public SistemaGestionProductos(HashMap<Integer, Producto> products, HashMap<Integer, Proveedor> suppliers,
			HashMap<Integer, OrdenDeCompra> purchaseOrders) {
		this.products = products;
		this.suppliers = suppliers;
		this.purchaseOrders = purchaseOrders;
	}

	public HashMap<Integer, Producto> getProducts() {
		return products;
	}

	public void setProducts(HashMap<Integer, Producto> products) {
		this.products = products;
	}

	public HashMap<Integer, Proveedor> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(HashMap<Integer, Proveedor> suppliers) {
		this.suppliers = suppliers;
	}

	public HashMap<Integer, OrdenDeCompra> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(HashMap<Integer, OrdenDeCompra> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public void modificarPrecioProducto(int code, double unitPrice) {
		Producto product = products.get(code);
		if (product != null)
			product.setUnitPrice(unitPrice);
	}

	public void modificarDescrProducto(int code, String descr) {
		Producto product = products.get(code);
		if (product != null)
			product.setDescription(descr);
	}

	public void eliminarProducto(int code) {
		products.remove(code);
	}

	public void agregarProducto(Producto product) {
		if (!(products.containsKey(product.getCode()))) {
			products.put(product.getCode(), product);
		}
	}

	public int BuscarProovedores(Proveedor supplier) {
		int result = 0;
		for (OrdenDeCompra purchaseOrder : purchaseOrders.values()) {
			if (purchaseOrder.getSupplier().equals(supplier))
				result++;
		}
		return result;
	}

}
