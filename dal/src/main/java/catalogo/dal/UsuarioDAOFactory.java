package catalogo.dal;

public class UsuarioDAOFactory {

	public static UsuarioDAO getUsuarioDAO() {

		UsuarioDAO usuarios = new UsuarioDAOMySQL();

		return usuarios;

	}
	
	public static UsuarioDAO getUsuarioDAO(String url) {
		return new UsuarioDAOMySQL(url);
	}
}
