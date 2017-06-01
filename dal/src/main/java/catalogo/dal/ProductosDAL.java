package catalogo.dal;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import catalogo.tipos.Producto;

public interface ProductosDAL {

	public void alta(Producto producto);

	public void modificar(Producto producto);

	public void borrar(Producto producto);

	public Producto buscarPorId(Integer idmap);

	public Producto[] buscarTodosLosProductos();

	public boolean validar(Producto producto);
	
	public HashMap<String, Producto> referenciarPorNombre();
	
	public Map<String, Queue<Producto>> getAlmacen();
}
