package catalogo.dal;

import catalogo.tipos.Usuario;

public interface UsuariosDAL {
	public void alta(Usuario usuario);

	public void modificar(Usuario usuario);

	public void borrar(Usuario usuario);

	public Usuario buscarPorId(String id);

	public Usuario[] buscarTodosLosUsuarios();

	public boolean validar(Usuario usuario);
	
	public boolean validarNombre (Usuario usuario);
}
