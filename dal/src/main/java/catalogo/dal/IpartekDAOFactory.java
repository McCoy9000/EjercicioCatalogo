package catalogo.dal;

public class IpartekDAOFactory {
	public static IpartekDAO getIpartekDAO() {
		return new IpartekDAOMySQL();
	}
}
