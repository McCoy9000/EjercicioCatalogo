package catalogo.tipos;

import java.math.BigDecimal;

import catalogo.constantes.Constantes;

public class Articulo extends Producto {

	private static final long serialVersionUID = 1281671186514962969L;

	int cantidad;

	public Articulo() {
		super();
		this.setPrecio(getPrecio().multiply(new BigDecimal(Constantes.IVA + 1)));
		this.cantidad = 1;
	}

	public Articulo(int groupId, String nombre, String descripcion, BigDecimal precio, int cantidad) {
		super(groupId, nombre, descripcion, precio);
		this.setPrecio(getPrecio().multiply(new BigDecimal(Constantes.IVA + 1)));
		this.cantidad = cantidad;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
