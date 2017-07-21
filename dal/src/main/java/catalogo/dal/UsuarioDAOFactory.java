package catalogo.dal;

public class UsuarioDAOFactory {

	public static UsuarioDAO getUsuarioDAO() {

		UsuarioDAO usuarios = new UsuarioDAOMySQL();

		return usuarios;

	}
	
	public static UsuarioDAO getUsuarioDAO(String url) {
		return new UsuarioDAOMySQL(url);
	}
	
	public static UsuarioDAO getCompradorDAO(String url) {
		return new CompradorDAOMySQL(url);
	}
	
	public static UsuarioDAO getCompradorDAO() {

		UsuarioDAO usuarios = new CompradorDAOMySQL();

		return usuarios;

	}
}
