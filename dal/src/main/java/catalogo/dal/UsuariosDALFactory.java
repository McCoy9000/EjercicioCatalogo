package catalogo.dal;

public class UsuariosDALFactory {

	public static UsuarioDAO getUsuarioDAO() {
		// return new UsuariosDALUsuarioUnico();
		UsuarioDAO usuarios = new UsuarioDAOMySQL();

		return usuarios;

	}
}
