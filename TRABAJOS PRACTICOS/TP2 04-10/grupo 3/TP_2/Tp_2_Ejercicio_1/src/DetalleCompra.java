public class DetalleCompra {
    private Producto producto;
    private int cantidad;
    public  DetalleCompra(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getCantidad() {
        return cantidad;
    }
    public Producto getProducto() {
        return producto;
    }

}
