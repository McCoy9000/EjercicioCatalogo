package catalogo.dal;

import java.util.List;
import java.util.Map;

import catalogo.tipos.Producto;

public interface CarritoDAO {

	public void anadirAlCarrito(Producto producto);
	
	public void quitarDelCarrito(Integer idmap);

	public Producto buscarPorId(Integer idmap);

	public Producto[] buscarTodosLosProductos();
	
	public Double precioTotal();
	
	public Map<Integer, List<Producto>> getAlmacen();
	
	public int getStock(Producto producto);

	public Producto[] getCatalogo();

}
