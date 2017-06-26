package catalogo.tipos;

public class Articulo extends Producto {

	private static final long serialVersionUID = 1281671186514962969L;

	int cantidad;

	public Articulo() {
		super();
		this.cantidad = 1;
	}

	public Articulo(int groupId, String nombre, String descripcion, double precio, int cantidad) {
		super(groupId, nombre, descripcion, precio);
		this.cantidad = cantidad;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
