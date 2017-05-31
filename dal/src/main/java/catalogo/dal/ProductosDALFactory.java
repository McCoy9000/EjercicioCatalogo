package catalogo.dal;

public class ProductosDALFactory {
	public static ProductosDAL getProductosDAL() {
		return new ProductosDALFichero();
	}
}
