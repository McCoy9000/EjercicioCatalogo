package catalogo.dal;

public class UsuarioDAOFactory {

	public static UsuarioDAO getUsuarioDAO() {

		UsuarioDAO usuarios = new UsuarioDAOHibernate();

		return usuarios;

	}
}
