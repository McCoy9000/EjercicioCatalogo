package catalogo.tipos;

import java.math.BigDecimal;

import catalogo.constantes.Constantes;

public class Articulo extends Producto {

	private static final long serialVersionUID = 1281671186514962969L;

	int cantidad;

	public Articulo() {
		super();
		BigDecimal bd = getPrecio().multiply(Constantes.IVA.add(BigDecimal.ONE));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		this.setPrecio(bd);		
		this.cantidad = 1;
	}

	public Articulo(int id, int groupId, String nombre, String descripcion, BigDecimal precio, int cantidad) {
		super(id, groupId, nombre, descripcion, precio);
		BigDecimal bd = getPrecio().multiply(Constantes.IVA.add(BigDecimal.ONE));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		this.setPrecio(bd);
		this.cantidad = cantidad;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
