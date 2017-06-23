package catalogo.tipos;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class Carrito implements Serializable {

	private static final long serialVersionUID = -5230603925290381578L;

	private static Logger log = Logger.getLogger(Carrito.class);

	private HashMap<Integer, Producto> listaProductos;

	public Carrito() {
		this.listaProductos = new HashMap<>();
		log.info("Creado carrito con lista de productos vacia");
	}

	public Carrito(HashMap<Integer, Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public HashMap<Integer, Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(HashMap<Integer, Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	@Override
	public String toString() {
		return "listaProductos=" + listaProductos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listaProductos == null) ? 0 : listaProductos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carrito other = (Carrito) obj;
		if (listaProductos == null) {
			if (other.listaProductos != null)
				return false;
		} else if (!listaProductos.equals(other.listaProductos))
			return false;
		return true;
	}

}
