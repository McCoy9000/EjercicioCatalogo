package catalogo.dal;

import java.util.List;

import catalogo.tipos.Usuario;

public class UsuarioDAOHibernate extends IpartekDAOHibernate implements UsuarioDAO {

	public UsuarioDAOHibernate(){
		// TODO Auto-generated constructor stub
	}

	@Override
	public Usuario[] findAll() {
		@SuppressWarnings("unchecked")
		List<Usuario> usuariosList = (List<Usuario>)manager.createQuery("FROM usuarios").getResultList();
		
		return usuariosList.toArray(new Usuario[usuariosList.size()]);
	}

	@Override
	public Usuario findById(int id) {
		
		return manager.find(Usuario.class, id);
	}

	@Override
	public Usuario findByName(String username) {
		
		return manager.find(Usuario.class, username);
	}

	@Override
	public int insert(Usuario usuario) {
		
		manager.persist(usuario);
		return 0;
	}

	@Override
	public void update(Usuario usuario) {
		Usuario u = manager.find(Usuario.class, usuario.getId());
		u.setId_roles(usuario.getId_roles());
		u.setNombre_completo(usuario.getNombre_completo());
		u.setPassword(usuario.getPassword());
		u.setUsername(usuario.getUsername());
	}

	@Override
	public void delete(Usuario usuario) {
		manager.remove(usuario);
		
	}

	@Override
	public void delete(int id) {
		manager.remove(manager.find(Usuario.class, id));
	}

	@Override
	public void deleteUsuarios() {
		
	}

	@Override
	public boolean validar(Usuario usuario) {
		if (manager.contains(usuario)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean validarNombre(Usuario usuario) {
		for (Usuario u: this.findAll()) {
			if (u.getUsername().equals(usuario.getUsername())) {
				return true;
			}
		}
		return false;
	}
}
