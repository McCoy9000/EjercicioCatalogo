package catalogo.dal;

public class ProductosDALFactory {
	public static ProductoDAO getProductosDAL() {
		return new ProductoDAOMySQL();
	}
}
