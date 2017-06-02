package catalogo.dal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import catalogo.tipos.Producto;

public class ProductosDALColeccion implements ProductosDAL {

	private Map<Integer, Producto> productos = new TreeMap<Integer, Producto>();
	private Map<String, Queue<Producto>> almacen = new HashMap<>();
	
	public void alta(Producto producto) {
		if (productos.containsKey(producto.getId()))
			throw new ProductoYaExistenteDALException("Ya existe el producto " + producto.getNombre());
		productos.put(producto.getId(), producto);
		
		if (almacen.containsKey(producto.getNombre())){
			Queue<Producto> stock = almacen.get(producto.getNombre());
			stock.offer(producto);
			almacen.put(producto.getNombre(), stock);
		} else {
			Queue<Producto> stock = new LinkedList<>();
			almacen.put(producto.getNombre(), stock);
		}
		
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

	public HashMap<String, Producto> referenciarPorNombre() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, List<Producto>> getAlmacen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStock(Producto producto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Producto[] getCatalogo() {
		// TODO Auto-generated method stub
		return null;
	}

}
