package catalogo.dal;

public class DireccionDAOFactory {

	public static DireccionDAO getDireccionDAO() {

		DireccionDAO direcciones = new DireccionDAOMySQL();

		return direcciones;

	}
	
	public static DireccionDAO getDireccionDAO(String url) {
		return new DireccionDAOMySQL(url);
	}
}
