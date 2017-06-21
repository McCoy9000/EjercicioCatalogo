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
}
