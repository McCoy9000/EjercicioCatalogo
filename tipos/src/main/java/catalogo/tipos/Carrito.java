package catalogo.tipos;

import java.util.HashMap;
import org.apache.log4j.Logger;


public class Carrito {

	private static Logger log = Logger.getLogger(Carrito.class);
	
	HashMap<Integer, Producto> listaProductos;
	
	public Carrito() {
		super();
		this.listaProductos = new HashMap<>();
		log.info("Creado carrito con lista de productos vacía");
	}
	
	public Carrito(HashMap<Integer, Producto> listaProductos) {
		super();
		this.listaProductos = listaProductos;
	}
	
	public void anadirAlCarrito(Producto producto) {
		
		listaProductos.put(producto.getId(), producto);
	}
	
	public void quitarDelCarrito (Integer idmap) {
		
		listaProductos.remove(idmap);
	}
	
	public Producto buscarPorId (Integer idmap) {
		
		return listaProductos.get(idmap);
	}
	
	public Producto[] buscarTodosLosProductos() {
		return listaProductos.values().toArray(new Producto[listaProductos.size()]);
	}
	
	public Double precioTotal() {
		
		Producto[] listaProductosArr = this.buscarTodosLosProductos();
		
		Double precioTotal = 0.0;
				
		for (Producto p : listaProductosArr) {
			
			precioTotal += p.getPrecio();
			
		}
		
		return precioTotal;
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
		result = prime * result
				+ ((listaProductos == null) ? 0 : listaProductos.hashCode());
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
