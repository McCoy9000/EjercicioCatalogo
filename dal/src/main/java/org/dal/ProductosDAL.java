package org.dal;

import org.tipos.Producto;

public interface ProductosDAL {

	public void alta(Producto producto);

	public void modificar(Producto producto);

	public void borrar(Producto producto);

	public Producto buscarPorId(Integer idmap);

	public Producto[] buscarTodosLosProductos();

	public boolean validar(Producto producto);

}
