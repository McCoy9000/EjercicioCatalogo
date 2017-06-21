package catalogo.dal;

public class FacturaDAOFactory {
	public static FacturaDAO getFacturaDAO() {
		return new FacturaDAOMySQL();
	}
}
