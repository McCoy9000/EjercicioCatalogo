package catalogo.dal;

public class ProductoDAOFactory {
	public static ProductoDAO getProductoDAO() {
		return new ProductoDAOMySQL();
	}
	
	public static ProductoDAO getProductoReservadoDAO() {
		return new ProductoReservadoDAOMySQL();
	}
	
	public static ProductoDAO getProductoVendidoDAO() {
		return new ProductoVendidoDAOMySQL();
	}
	
	public static ProductoDAO getProductoDAO(String url) {
		return new ProductoDAOMySQL(url);
	}
	
	public static ProductoDAO getProductoReservadoDAO(String url) {
		return new ProductoReservadoDAOMySQL(url);
	}
	
	public static ProductoDAO getProductoVendidoDAO(String url) {
		return new ProductoVendidoDAOMySQL(url);
	}

}
