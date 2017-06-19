package catalogo.dal;

public class CarritoDAOFactory {
	public static CarritoDAO getCarritoDAO() {
		return new CarritoDAOColeccion();
	}
}
