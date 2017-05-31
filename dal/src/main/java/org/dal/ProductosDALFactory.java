package org.dal;

public class ProductosDALFactory {
	public static ProductosDAL getProductosDAL() {
		return new ProductosDALFichero();
	}
}
