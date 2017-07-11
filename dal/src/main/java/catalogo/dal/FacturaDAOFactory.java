package catalogo.dal;

public class FacturaDAOFactory {
	public static FacturaDAO getFacturaDAO() {
		return new FacturaDAOMySQL();
	}
	
	public static FacturaDAO getFacturaDAO(String url) {
		return new FacturaDAOMySQL(url);
	}
}
