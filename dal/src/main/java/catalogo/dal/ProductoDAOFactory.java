package catalogo.dal;

public class ProductoDAOFactory {
	public static ProductoDAO getProductoDAO() {
		return new ProductoDAOMySQL();
	}
}
