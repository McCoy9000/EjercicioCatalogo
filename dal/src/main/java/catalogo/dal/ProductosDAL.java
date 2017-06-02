package catalogo.dal;

import java.util.List;
import java.util.Map;

import catalogo.tipos.Producto;

public interface ProductosDAL {

	public void alta(Producto producto);

	public void modificar(Producto producto);

	public void borrar(Producto producto);

	public Producto buscarPorId(Integer idmap);

	public Producto[] buscarTodosLosProductos();

	public boolean validar(Producto producto);
	
	public Map<Integer, List<Producto>> getAlmacen();
	
	public int getStock (Producto producto);
	
	public Producto[] getCatalogo();
}
