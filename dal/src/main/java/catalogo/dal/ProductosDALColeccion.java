package catalogo.dal;

import java.util.Map;
import java.util.TreeMap;

import catalogo.tipos.Producto;

public class ProductosDALColeccion implements ProductosDAL {

	private Map<Integer, Producto> productos = new TreeMap<Integer, Producto>();

	public void alta(Producto producto) {
		if (productos.containsKey(producto.getId()))
			throw new ProductoYaExistenteDALException("Ya existe el producto " + producto.getNombre());

		productos.put(producto.getId(), producto);
		Producto.siguienteId++;
	}

	public void modificar(Producto producto) {
		if (!productos.containsKey(producto.getId()))
			throw new DALException("Intento de modificar producto no existente " + producto);

		productos.put(producto.getId(), producto);
	}

	public void borrar(Producto producto) {
		productos.remove(producto.getId());
	}

	public Producto buscarPorId(Integer idmap) {
		return productos.get(idmap);
	}

	public Producto[] buscarTodosLosProductos() {
		return productos.values().toArray(new Producto[productos.size()]);
	}

	public boolean validar(Producto producto) {
		return productos.containsValue(producto);
	}

}
