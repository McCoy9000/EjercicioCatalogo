package catalogo.dal;

import java.util.List;
import java.util.Map;

import catalogo.tipos.Articulo;
import catalogo.tipos.Producto;

public interface ProductoDAO extends IpartekDAO {
	public Producto[] findAll();

	public Producto findById(int id);

	public Producto findByName(String nombre);

	public int insert(Producto producto);

	public void update(Producto producto);

	public void delete(Producto producto);

	public void delete(int id);

	public void deleteProductos();

	public boolean validar(Producto producto);

	public boolean validarNombre(Producto producto);

	public Map<Integer, List<Producto>> getAlmacen();

	public int getStock(Producto producto);

	public Articulo[] getCatalogo();
}
