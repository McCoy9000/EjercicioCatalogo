package catalogo.dal;

import catalogo.tipos.Producto;

public interface ProductoDAO extends IpartekDAO {
	public Producto[] findAll();

	public Producto findById(int id);

	public Producto findByName(String nombre);

	public int insert(Producto producto);

	public void update(Producto producto);

	public void delete(Producto producto);

	public void delete(String nombre);

	public boolean validar(Producto producto);

	public boolean validarNombre(Producto producto);
}
