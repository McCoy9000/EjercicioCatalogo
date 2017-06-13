package catalogo.dal;

import catalogo.tipos.Usuario;

public interface UsuarioDAO extends IpartekDAO {
	public Usuario[] findAll();

	public Usuario findById(int id);

	public Usuario findByName(String username);

	public int insert(Usuario usuario);

	public void update(Usuario usuario);

	public void delete(Usuario usuario);

	public void delete(String username);

	public void deleteUsuarios();

	public boolean validar(Usuario usuario);

	public boolean validarNombre(Usuario usuario);
}
